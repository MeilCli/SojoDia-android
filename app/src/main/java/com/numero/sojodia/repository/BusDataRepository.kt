package com.numero.sojodia.repository

import android.content.Context

import com.numero.sojodia.model.BusDataFile
import com.numero.sojodia.model.BusTime

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.StringTokenizer

import io.reactivex.Observable

class BusDataRepository(private val context: Context) : IBusDataRepository {

    override fun loadBusData(busDataFile: BusDataFile): Observable<List<BusTime>> {
        return Observable.create {
            val dataList = ArrayList<BusTime>()
            val bufferedReader: BufferedReader
            var fileInputStream: FileInputStream? = null
            var inputStream: InputStream? = null

            // ダウンロードしたファイルがなければアセットから取得
            val file = File(context.filesDir.toString() + "/" + busDataFile.fileName)
            if (file.exists()) {
                fileInputStream = FileInputStream(file)
                bufferedReader = BufferedReader(InputStreamReader(fileInputStream, "UTF-8"))
            } else {
                val assetManager = context.resources.assets
                inputStream = assetManager.open(busDataFile.fileName)
                bufferedReader = BufferedReader(InputStreamReader(inputStream!!))
            }

            var isFirstLine = true
            while (true) {
                val line = bufferedReader.readLine() ?: break
                if (isFirstLine) {
                    // 最初の行はデータではないためスキップ
                    isFirstLine = false
                    continue
                }
                val tokenizer = StringTokenizer(line, ",")
                val hour = tokenizer.nextToken().toInt()
                val minutes = tokenizer.nextToken().toInt()
                val week = tokenizer.nextToken().toInt()
                val isNonstop = tokenizer.nextToken().toInt() != 0

                dataList.add(BusTime(hour, minutes, week, isNonstop))
            }

            bufferedReader.close()
            fileInputStream?.close()
            inputStream?.close()
            it.onNext(dataList)
        }
    }
}
