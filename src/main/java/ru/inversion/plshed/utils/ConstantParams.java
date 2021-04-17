package ru.inversion.plshed.utils;

public class ConstantParams {

    public static final String classString =
                    "package ru.inversion.plshed.entity.lovEntity;\n" +
                    "import javax.persistence.*;\n" +
                    "import java.io.Serializable;\n" +
                    "\n" +
                    "/**\n" +
                    "@author  XDWeloper\n" +
                    "@since   2021/04/16 16:29:26\n" +
                    "*/\n" +
                    "@Entity (name=\"ru.inversion.plshed.entity.lovEntity.PDual\")\n" +
                    "@Table(name=\"DUAL\")\n" +
                    "@NamedNativeQuery(name = \"query\", query = \":QUERY\")\n" +
                    "public class :CLASSNAME implements Serializable\n" +
                    "{\n" +
                    "    private static final long serialVersionUID = 16_04_2021_16_29_26l;\n" +
                    "\n" +
                    "    private String VAL;\n" +
                    "    private String DESCR;\n" +
                    "\n" +
                    "    public :CLASSNAME(){}\n" +
                    "\n" +
                    "    @Id \n" +
                    "    @Column(name=\"VAL\",length = 1)\n" +
                    "    public String getVAL() {\n" +
                    "        return VAL;\n" +
                    "    }\n" +
                    "    public void setVAL(String val) {\n" +
                    "        VAL = val; \n" +
                    "    }\n" +
                    "    @Id \n" +
                    "    @Column(name=\"DESCR\",length = 7)\n" +
                    "    public String getDESCR() {\n" +
                    "        return DESCR;\n" +
                    "    }\n" +
                    "    public void setDESCR(String val) {\n" +
                    "        DESCR = val; \n" +
                    "    }\n" +
                    "}\n";

}
