# e-commerce-sites-demo
练手项目

##问题记录
###1 Idea在pom.xml中添加了mybatis-generator插件后，右侧的maven projects中并未显示这个插件。
解决方法: 新建一个plugins标签和pluginManager同级，然后把generator插件配置放进去。
###2 数据库配置驱动路径报错提示未找到。
解决方法：注意，如果从maven本地仓库拉取时，后缀不带有bin，所以如果配置的时候要检查清楚文件名和所配路径。

##其他操作
###1 去除sql黄色辣眼睛背景
https://www.cnblogs.com/prettrywork/p/7904378.html
