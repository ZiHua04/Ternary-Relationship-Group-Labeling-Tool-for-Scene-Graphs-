# 项目介绍
    通过本项目能实现对图像的自定义物体标注，且可以生成不同物体间的关系，最终形成能够用于场景图模型训练的自定义数据集。
 
 
# 环境依赖
### 本项目使用Alibaba的fastJSON作为JSON解析库
     
     <dependencies>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.47</version>
        </dependency>
    </dependencies>
 
# 目录结构描述
    ├── ReadMe.md           // 帮助文档
    
    ├── idea    // idea项目配置
    
    ├── resources             // 数据存储处
    
    │   ├── images     // 用于存放待标记的图片
    
    │       ├── 1.png
    
    │       └── 2.png

    │       └── ......
    
    │   └── Json             // 用于存放标记后的json文件
    
    │       ├── rel.json       // 三元关系标注信息
    
    │       └── train.json         // 训练标注信息

    │       └── val.json         // 验证标注信息

    │       └── test.json         // 测试标注信息

    ├── src
        ├── main
            ├── java
            
                ├── Annotation.java
                ├── CategoriesLoader.java
                ├── Category.java
                ├── Config.java           //配置
                ├── ImageLoader.java
                ├── ImagePanel.java
                ├── Main.java        //标注工具入口
                ├── MainFrame.java
                ├── MyImage.java
                ├── ReadJsonFile.java        //辅助移动标记工具
                ├── Rel_category.java
                ├── RelationPanel.java
                ├── Rename.java        //图像重命名工具

    ├── target              // 合成结果存放的文件夹
    
    └── pom.xml                // maven配置
 
# 使用说明

### 1. 准备图片与标签
##### a
    将待标注的图片全部复制粘贴到项目的“resources/images”文件夹下
##### b
    运行Rename.java对图像进行重命名，得到以下格式的图片。
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/a10279fd-2818-4e3d-b98a-368d0f428bd3)
##### c
    修改“resources/categories.txt” 与 “resources/rel_categories.txt”文件，将你需要标注的种类与种类之间的关系分别如下填写到txt中：
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/2eebf439-2e32-4a11-b472-32556ba13c94)
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/79747434-cded-4968-be4b-35ee88352856)

##### d
    检查“resources/Json”中的rel.json, train.json, val.json, test.json 是否为下图空格式，若否则手动修改。
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/8a2e5866-3230-4c41-98c3-915b5341dda9)
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/895974e0-aadb-46d6-ac14-17658b6c9b53)

### 2. 开始标注
##### a. 运行
    运行main.java,出现窗口则运行成功
##### b. 标注
    在右侧下拉选择框中选择需要标记的种类。
    选择后，在图片中需要标记处长按鼠标并拖动，直到红框将物体全部框住。
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/fc93276a-3f43-43ef-9dac-0489b0a948c3)
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/269ca9f8-2466-4562-8d1b-776272baba45)
#### c. 选择关系
当前图片中存在例如`“穿”`，`“拿”`等有关系的两个物体时，点击右侧的`选择关系`按钮，在弹出的窗口中进行关系的选择。
<br>点击`确定添加`则将该三元关系组，添加进rel.json中</br>
![image](https://github.com/ZiHua04/Ternary-Relationship-Group-Labeling-Tool-for-Scene-Graphs/assets/128723416/b00d2516-4cf2-48f2-9711-7aceecc6b8b6)

### 3. 数据处理
##### a
    若图片数较少，可手动将train.json中的数据剪切到val.json和test.json中，同时rel.json中的train中的三元关系组也需要对应剪切到val和test中。
##### b
    若图片数量较多，本项目也准备自动剪切工具
    
    运行ReadJsonFile.java， train,val,test就自动按8：1：1的比例分配进入json文件中。即可直接使用

 
 
