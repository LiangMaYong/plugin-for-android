# plugin-for-android
plugin-for-android是一个安卓插件化开发框架，支持启动未安装的apk，本框架代码可供参考学习，本框架遵循MIT开源协议，几如果你要直接在你的项目中使用，请在项目中显示［plugin－for－android 提供插件化支持］字样。

欢迎 star 和 [Issues](https://github.com/LiangMaYong/plugin-for-android/issues)

## 版本说明
[![LatestVersion](https://img.shields.io/badge/LatestVersion-1.0.0-brightgreen.svg?style=plastic) ](https://github.com/LiangMaYong/plugin-for-android/)

| 版本 |日期| 说明 |
|---|---|---|
| 1.0.0|2016-07-07| 测试版本 |
## gradle
插件化核心包［必选］
```
dependencies {
    compile 'com.liangmayong:androidplugin:1.0.0'
}
```
插件化拓展包［可选］
```
dependencies {
   compile 'com.liangmayong:androidplugin-support:1.0.0'
}
```
插件化插件包［可选］
```
dependencies {
    compile 'com.liangmayong:androidplugin-bundle:1.0.0'
}
```

##参考
[android-pluginmgr](https://github.com/houkx/android-pluginmgr)

[dynamic-load-apk](https://github.com/singwhatiwanna/dynamic-load-apk)

##Demo APK
[plugin-demo.apk](https://raw.githubusercontent.com/LiangMaYong/plugin-for-android/master/plugin-demo.apk)
##感谢
@Loby
##技术交流
QQ群：297798093

email：ibeam@qq.com
##License
```
The MIT License (MIT)

Copyright (c) 2016 LiangMaYong

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
