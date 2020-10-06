import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File

fun m3u8download(url: String, outputPath: String, concurNum: Int = 50) = GlobalScope.launch {
    val client = HttpClient(CIO)
    val m3u8Header: String = client.get {
        url(url)
    }
    val contents = m3u8Header.split('\n')
    if(contents.isEmpty()){
        println("contents is empty.")
        return@launch
    }
    if (!m3u8formatCheck(contents)){
        println("the file is not m3u8.")
        return@launch
    }
    val currentPath = url.substring(0, url.indexOfLast { it=='/' })
    val file = File("$outputPath/content.ts").outputStream()
    val channel = Channel<Deferred<ByteArray>>(capacity = concurNum)
    //1st, to get all links
    val links = contents.filter { !(it.startsWith('#') || it.isEmpty()) }
    async {
        links.forEach {
            channel.send(async {
                client.get<ByteArray> {
                    url("$currentPath/$it")
                }
            })
        }
        channel.close()
    }
    if(links.isEmpty()){
        error("no video links")
        return@launch
    }
    var cc = 0
    var value: Double
    while (!channel.isClosedForReceive){
        val item = channel.receive().await()
        cc++
        value = cc.toDouble()/links.size*100
        print("\rdownloading... ${"%.2f".format(value)}%")
        file.write(item)
    }
    println()
    file.close()
    client.close()
}

fun m3u8formatCheck(contents: List<String>):Boolean{
    if(!contents[0].startsWith("#EXTM3U")){
        return false
    }
    return true
}