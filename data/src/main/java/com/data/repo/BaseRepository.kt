package com.data.repo

import android.content.Context
import com.data.model.AppDataBase

open class BaseRepository(protected val ctx: Context) {
    protected val dao = AppDataBase.getInstance(ctx).weatherDao()

}
