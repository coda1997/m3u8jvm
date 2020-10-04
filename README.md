# A simple tool for downloading m3u8 video.

- It supports multi-thread downloading, and you can customize the thread number according to your own computer.
- Also, it gives an easy code implement for m3u8 downloader, you can use for whatever.
- However, it has an ugly code style, which I may fix it further. Anyway, it works.

## Usages

``` shell script
java -jar m3u8d.jar "sample.m3u8" -o:outputpath -c:thread-number
```