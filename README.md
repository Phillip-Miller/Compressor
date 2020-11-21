# Compressor - Phillip Miller

Compressor is a pair of java applications (Compress.java, Decompress.java) that preforms lossless compression/decompression on any ascii file of your choice.
This program works by using a longest reoccurring substring, which will only provide notable compression at fairly large file sizes.

Compress was written using a a HashTable class I wrote called HashTableChain.

## Usage

```bash
javac *.java
Compress yourFile.txt //Compresses your file into yourFile.zzz and produces a log file
Decompress yourFile.txt.zzz //Lossless decompression back into the original file, produce another log file
```

## Project Status
This is a finished project. Feel free to do what you want with it.

##License
GNU General Public License v3.0
