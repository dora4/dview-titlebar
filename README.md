dview-titlebar
![Release](https://jitpack.io/v/dora4/dview-titlebar.svg)
--------------------------------

#### gradle依赖配置

```groovy
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dview-titlebar:1.8'
}
```

#### 控件使用

```kotlin
     val imageView = AppCompatImageView(this)
        val dp24 = DensityUtils.dp2px(this, 24f)
        imageView.layoutParams = RelativeLayout.LayoutParams(dp24, dp24)
        imageView.setImageResource(R.drawable.ic_save)
        val imageView2 = AppCompatImageView(this)
        imageView2.layoutParams = RelativeLayout.LayoutParams(dp24, dp24)
        imageView2.setImageResource(R.drawable.ic_confirm)
        mBinding.titleBar
            .addMenuButton(imageView)
            .addMenuButton(imageView2)
            .setOnIconClickListener(object : DoraTitleBar.OnIconClickListener {
            override fun onIconBackClick(icon: AppCompatImageView) {
                LogUtils.i("返回")
            }

            override fun onIconMenuClick(position: Int, icon: AppCompatImageView) {
                LogUtils.i("点击了第${position}个菜单")
            }
        })
    }
```
