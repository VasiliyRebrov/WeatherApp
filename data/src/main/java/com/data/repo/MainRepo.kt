package com.data.repo

import android.content.Context
import kotlinx.coroutines.flow.map

class MainRepo(ctx: Context) : BaseRepo(ctx) {
    val cities = dao.getLiveCityList().map { it.sortedBy { city -> city.serialNumber } }
    //сейчас здесь просто преобразование
    //но что если учесть случай, когда у нас простая пересортировка списка, и не следует слать в ливдату новые данные
    //попытаться текущий flow сначала пересортировать, но не по serialnumber, а по id, а потом учесть distinct.
    //получится так, что пересортированный список, тк. для нас он не менялся, не будет поступать дальше
    //это пока предположение. мб и нет смысла. ничего не стоит, просто перепроверить в методе
}
