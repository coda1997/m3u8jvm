import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        error("please enter the url.")
        return
    }
    val url = args[0]
    var outputPath = "."
    var c = 25
    if (args.size > 1) {
        for (i in 1 until args.size - 1) {
            if (args[i].startsWith("-o")) {
                outputPath = args[i].substring(args[i].indexOf(':') + 1)
            }
            if (args[i].startsWith("-c")) {
                c = args[i].substring(args[i].indexOf(':') + 1).toInt()
            }
        }
    }
    println("config info: url = $url, output path = $outputPath, number = $c")
    val job = m3u8download(url, outputPath, c)
    while (!job.isCompleted){

    }
    println("done")

}