<#assign json="${data}"?eval />
package ${json.packageName};

import java.io.Serializable;
import java.util.List;

public class ${convertStr(json.className, "lowerCamel")} implements Serializable {

    <#list json.fields as field>
        private ${typeTransfer(field.type)} ${convertStr(field.attrName, "lowerCamel")};
    </#list>

    <#list json.fields as field>

        public ${typeTransfer(field.type)} get${convertStr(field.attrName, "upperCamel")}() {
            return ${convertStr(field.attrName, "lowerCamel")};
        }

        public void set${convertStr(field.attrName, "upperCamel")}(${typeTransfer(field.type)} ${convertStr(field.attrName, "lowerCamel")}) {
            this.${convertStr(field.attrName, "lowerCamel")} = ${convertStr(field.attrName, "lowerCamel")};
        }
    </#list>

}
