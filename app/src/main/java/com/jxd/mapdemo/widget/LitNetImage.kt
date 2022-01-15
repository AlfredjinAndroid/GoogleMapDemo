package com.jxd.mapdemo.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.jxd.mapdemo.R
import java.io.File



/**
 * @author : Alfred
 * 图片加载封装
 * 基于 Coil 图片加载
 * @param path 图片路径，可为网络或本地地址
 * @param placeHolder 占位图
 * @param error 加载错误占位
 * @param contentScale 图片展示类型
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun LitNetImage(
    modifier: Modifier = Modifier,
    path: String?,
    @DrawableRes placeHolder: Int = R.drawable.ic_launcher_foreground,
    @DrawableRes error: Int = placeHolder,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val p = path ?: ""
    Image(painter = rememberImagePainter(data = if (p.startsWith("http")) p else File(p), builder = {
        placeholder(placeHolder)
        error(error)
    }), contentDescription = path.toString(), modifier = modifier, contentScale = contentScale)
}