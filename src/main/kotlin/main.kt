fun main(args: Array<String>) {
    if(args.isEmpty()){
       error("please enter the url.")
        return
    }
    val url = args[0].substring(1 until args[0].length-1)
    var outputPath = "."
    var c = 25
    if(args.size>1){
        for (i in 1 until args.size-1){
            if(args[i].startsWith( "-o")){
                outputPath = args[i].substring(args[i].indexOf(':')+1)
            }
            if(args[i].startsWith( "-c")){
                c = args[i].substring(args[i].indexOf(':')+1).toInt()
            }
        }
    }
    println("config info: url = $url, output path = $outputPath, number = $c")
    m3u8download(url, outputPath, c)
}