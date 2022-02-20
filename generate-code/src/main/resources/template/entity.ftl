package ${packageName};

import java.io.Serializable;
import java.util.List;

public class ${entityName} implements Serializable {

    <#list fields as field>
        private ${field.type} ${convertStr(field.fieldName, "hump")};
    </#list>

    <#list fields as field>

        public ${field.type} ${convertStr(field.fieldName, "get")}() {
            return ${convertStr(field.fieldName, "hump")};
        }

        public void ${convertStr(field.fieldName, "set")}(${field.type} ${convertStr(field.fieldName, "hump")}) {
            this.${convertStr(field.fieldName, "hump")} = ${convertStr(field.fieldName, "hump")};
        }
    </#list>

}
