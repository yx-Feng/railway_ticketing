package com.example.railway.generator.server;

import com.example.railway.generator.util.DbUtil;
import com.example.railway.generator.util.Field;
import com.example.railway.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {

    static String serverPath = "[module]/src/main/java/com/example/railway/[module]/";

    static String pomPath = "generator/pom.xml";

    static {
        new File(serverPath).mkdir();
    }

    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }

    private static void gen(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target+".ftl"); // 读模板
        String toPath = serverPath + packageName + "/";
        new File(serverPath).mkdir();
        String Target = target.substring(0,1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param); // 生成
    }

    // 获取所有Java类型，使用Set去重
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for(int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }

    public static void main(String[] args) throws Exception {
        String generatorPath = getGeneratorPath(); // 获取mybatis-generator

        // 比如generator-config-member.xml，得到 module = member
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        System.out.println("serverPath:" + serverPath);
        // 读取table节点
        Document document = new SAXReader().read("generator/"+generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 为DbUtil设置数据源
        Node connectionURl = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        DbUtil.url = connectionURl.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();

        // 示例：表名 member_test
        // Domain = MemberTest
        String Domain = domainObjectName.getText();
        // domain = member_test
        String domain = Domain.substring(0,1).toLowerCase() + Domain.substring(1);
        // do_main = member-test
        String do_main = tableName.getText().replaceAll("_", "-");
        // 表中文名
        String tableNameCn = DbUtil.getTableComment(tableName.getText());
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);

        // 组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("fieldList", fieldList);
        param.put("typeSet", typeSet);
        System.out.println("组装参数：" + param);

//        gen(Domain, param, "service", "service");
//        gen(Domain, param, "controller", "controller");
        gen(Domain, param, "req", "saveReq");
//        gen(Domain, param, "req", "queryReq");
        gen(Domain, param, "resp", "queryResp");
    }
}
