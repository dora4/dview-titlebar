dview-titlebar
![Release](https://jitpack.io/v/dora4/dview-titlebar.svg)
--------------------------------

#### 卡片

![Dora视图_Title_Bar](https://github.com/user-attachments/assets/f34119d2-3a8c-4004-a118-b14d95664335)

#### Gradle依赖配置

```groovy
// 添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// 添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dview-titlebar:1.37'
}
```

#### 控件使用

```kotlin
        mBinding.titleBar
            .addMenuButton(R.drawable.ic_save)
            .addMenuButton(R.drawable.ic_confirm)
            .setOnIconClickListener(object : DoraTitleBar.OnIconClickListener {
            override fun onIconBackClick(icon: AppCompatImageView) {
                LogUtils.i("返回")
            }

            override fun onIconMenuClick(position: Int, icon: AppCompatImageView) {
                LogUtils.i("点击了第${position}个菜单")
            }
        })
```
