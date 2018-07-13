# e-commerce-sites-demo  
练手项目  
  
## 问题记录  
### 1 Idea在pom.xml中添加了mybatis-generator插件后，右侧的maven projects中并未显示这个插件。  
解决方法: 新建一个plugins标签和pluginManager同级，然后把generator插件配置放进去。  
### 2 数据库配置驱动路径报错提示未找到。  
解决方法：注意，如果从maven本地仓库拉取时，后缀不带有bin，所以如果配置的时候要检查清楚文件名和所配路径。  
### 3 Field injection is not recommended 引出的关于antowired的三中配置方法哪一种更好？  
https://blog.csdn.net/github_38222176/article/details/79506392  
### 4 @Autowired的用法详解  
https://blog.csdn.net/u013257679/article/details/52295106/  
### 5 expected at least 1 bean which qualifies as autowire candidate for this dependency 的异常。  
解决方法：  
1）检查各种注解是否添加，spring扫描包路径是否正确；  
2）如果修改了包名或者项目名，确保pom文件中build标签和一些关于修改文件名的标签内容匹配；  
3）spring的版本和ide不兼容；  
4）尝试降低Autowired的警告级别，因为上述第三点中提示有Spring团队不提倡直接在内部属性上加autowired。  


## 其他操作  
### 1 去除sql黄色辣眼睛背景  
https://www.cnblogs.com/prettrywork/p/7904378.html  
### 2 Guava Cache学习  
https://blog.csdn.net/u012881904/article/details/79263787  
