import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File

fun m3u8download(url: String, outputPath: String, concurNum: Int = 50) = GlobalScope.launch {
    val client = HttpClient()
    val m3u8Header: String = client.get {
        url(url)
    }
    val contents = m3u8Header.split('\n')
    if(contents.isEmpty()){
        error("contents is empty.")
        return@launch
    }
    if (!m3u8formatCheck(contents)){
        error("the file is not m3u8.")
        return@launch
    }
    val file = File("$outputPath/content.ts").outputStream()
    val channel = Channel<Deferred<ByteArray>>(capacity = concurNum)
    //1st, to get all links
    async {
        contents.filter { !(it.startsWith('#') || it.isEmpty()) }.forEach {
            channel.send(async {
                client.get<ByteArray>{
                    url(it)
                }
            })
        }
        channel.close()
    }
    while (true){
        val item = channel.receive().await()
        file.write(item)
        if(channel.isClosedForReceive){
            break
        }
    }
    file.close()
}

fun m3u8formatCheck(contents: List<String>):Boolean{
    if(!contents[0].startsWith("#EXTM3U")){
        return false
    }
    return true
}