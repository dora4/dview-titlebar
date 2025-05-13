dview-titlebar
![Release](https://jitpack.io/v/dora4/dview-titlebar.svg)
--------------------------------

#### 卡片
![DORA视图 标题栏](https://github.com/user-attachments/assets/4626b2db-dd46-4cbb-9884-c7af4caef1ba)
![DORA视图 DORA战士](https://github.com/user-attachments/assets/9233a84c-422a-408a-b812-acc2bb95ec4d)

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
```xml
        <dora.widget.DoraTitleBar
            android:id="@+id/titleBar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:dview_title="@string/common_title"
            app:dview_backIcon="@drawable/ic_back"
            app:dview_backIconSize="26dp"
            app:dview_isTitleTextBold="true"
            android:background="@color/colorPrimary"/>
```
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

#### 示例代码

https://github.com/dora4/dora_samples
