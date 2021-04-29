# KotlinDemo程序

## 程序信息
* 程序ID：com.jlpay.kotlinDemo
* 程序名称：kotlinDemo
* 更新时间：2021.02.03
* 更新人员：薛涛涛
* Key_Store_Password & Key_Password ：kotlinDemo
* Key_alias：mySignKey

## 程序功能
* Kotlin的日常练习

## 程序优化方向
* 1.添加腾讯Bugly完善崩溃和卡顿监控
* 2.添加美团日志上报功能，加强线上崩溃和卡顿原因追溯，用户操作行为追踪
* 3.添加友盟埋点，进行用户行为统计和分析
* 4.接入阿里性能组件监控
* 5.接入腾讯Tinker热更新
* 6.组件化

## 提交日志关键字说明
* feat：新功能（feature）。
* fix：修复bug，可以是QA发现的BUG，也可以是研发自己发现的BUG。
* chore：构建过程或辅助工具的变动。
* perf：优化相关，比如提升性能、体验

### tinker热更新使用指南

* 1.若开启混淆：则需要关闭R8，在gradle.properties文件中，加入android.enableR8.libraries=false，android.enableR8=false
两行代码

* 2.使用 assemblePro 命令打出生产验证的release包，在tinker-support.gradle文件中bakPath目录下生成基准包，
，直接删除其中的debug相关文件(apk文件和R文件)，将apk改名为app-pro-release.apk，然后复制保存bakApk目录下的文件

* 3.clean项目

* 4.创建app/build 目录，把保存的bakApk 拷贝进来

* 5.修改bug代码

* 6.修改tinker-support.gradle文件中两处地方：(1)def baseApkDir改成bakApk下面的文件名(第9行) (2)def customBaseApk
取有值的那一行代码(注释15行，放开16行)

* 7.执行 tinker-support命令下的 buildTinkerPatchProRelease 命令，然后在 app\build\outputs\patch\pro\release
目录下得到 patch_signed_7zip.apk包，即为补丁包

* 8.将上面获取到的补丁包上传Bugly平台进行测试，测试没问题后即可进行全量设备下发

* 注：Tinker的补丁包只针对特定的基准包tinkerId实现补丁，因为这里设置的tinkerId是自动生成的，所以即便代码相同，两次打出的基准包也是
不同的

* 常见问题1：打包apk时没有同步生成R.txt文件，在低版本的gradle中可以生成    
* 解决：路径 变了 /build/intermediates/runtime_symbol_list/prodRelease/R.txt，可以调用build.gradle中的copyTinkerRTxtFile任务执行复制

### ANT style pattern
Ant path 匹配原则
* 路径匹配原则(Path Matching) Spring MVC中的路径匹配要比标准的web.xml要灵活的多。默认的策略实现了 org.springframework.util.AntPathMatcher，就像名字提示的那样，
路径模式是使用了Apache Ant的样式路径，Apache Ant样式的路径有三种通配符匹配方法.这些可以组合出很多种灵活的路径模式

* ? 匹配任何单字符  
* * 匹配0或者任意数量的字符  
* ** 匹配0或者更多的目录 

* Example Ant-Style Path Patterns
* /app/*.x 匹配(Matches)所有在app路径下的.x文件  
* /app/p?ttern 匹配(Matches) /app/pattern 和 /app/pXttern,但是不包括/app/pttern  
* /**/example 匹配(Matches) /app/example, /app/foo/example, 和 /example  
* /app/**/dir/file. 匹配(Matches) /app/dir/file.jsp, /app/foo/dir/file.html,/app/foo/bar/dir/file.pdf, 和 /app/dir/file.java  
* /**/*.jsp 匹配(Matches)任何的.jsp 文件

```JSON
{

}
```

