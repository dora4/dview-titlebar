dview-titlebar
![Release](https://jitpack.io/v/dora4/dview-titlebar.svg)
--------------------------------

#### 卡片

![Dora视图_Title_Bar](https://github.com/user-attachments/assets/f34119d2-3a8c-4004-a118-b14d95664335)
![Dora视图_积木战士](https://github.com/user-attachments/assets/66763607-391b-43f3-bf39-6f59ab9da2f3)
##### 卡名：Dora视图 TitleBar 
###### 卡片类型：效果怪兽
###### 属性：地
###### 星级：4
###### 种族：岩石族
###### 攻击力/防御力：1300/2000
###### 效果：此卡不会因为对方卡的效果而破坏，并可使其无效化。此卡攻击里侧守备表示的怪兽时，若攻击力高于其守备力，则给予对方此卡原攻击力的伤害，并抽一张卡。此卡可以直接攻击对方玩家。

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
