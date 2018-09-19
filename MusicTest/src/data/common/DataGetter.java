package data.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import data.util.Configurator;

public class DataGetter extends AbstractDataGetter{

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new DataGetter().getData();
    }
    protected  void setFieldValues(){
        this.setOutFile(new File (Configurator.getProperty("outFileName")) );
        this.setInterfaceId(Configurator.getProperty("interfaceId"));
        HashMap params = new HashMap();
        for(Map.Entry entry:Configurator.getProperties().entrySet() ){
            String key = (String) entry.getKey();
            if( !key.startsWith("params."))
                continue;
            int index=key.indexOf('.')+1;
            params.put(key.substring(index), entry.getValue());
        }
        this.setParams(params);
    }
}
