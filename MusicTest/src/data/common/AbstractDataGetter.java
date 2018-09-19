package data.common;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import cma.cimiss.RetArray2D;
import cma.cimiss.client.DataQueryClient;
import data.util.Configurator;

public abstract class AbstractDataGetter {
    private final static String USER_ID = Configurator.getProperty("userName");
    private final static  String PASSWORD = Configurator.getProperty("password");
    
    private DataQueryClient   client;
    /* 2.2 接口ID */
    protected String interfaceId;
    /* 2.3 接口参数，多个参数间无顺序 */
    protected HashMap params;
    protected File outFile;

    public final void getData(){
        initSuccess();
        setFieldValues();
        String fields=(String)params.get("elements"); 
        try(BufferedWriter writer = new BufferedWriter( new FileWriter(outFile)) ){
            if(fields != null){
                writer.write(fields);
                writer.newLine();
            }
            getDataByTimeRange(writer);
        }catch(Exception e){
            e.printStackTrace();
        }
        destroyResources();
        System.out.println("Done!");
    }
    protected abstract void setFieldValues();
    private void getDataByTimeRange(BufferedWriter writer){
        SimpleDateFormat sdf=Configurator.DATE_FORMAT;
        Calendar[] timeRange=Configurator.getTimeRange();
        if(timeRange == null){
            callAPIAndWriteData(writer);
            return;
        }
        int interval = Configurator.getProperty("intervalInHours", 24);
        boolean isLastRange=false;
        Calendar beginTime = timeRange[0];
        while(!beginTime.after(timeRange[1])){
            Calendar endTime=(Calendar)beginTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, interval);
            String rangeRightBoundary=")";
            if(!endTime.before(timeRange[1])){
                isLastRange=true;
                endTime=timeRange[1];
                rangeRightBoundary="]";
            }
            String timeRangeStr="["+sdf.format(beginTime.getTime())
                    +"," + sdf.format(endTime.getTime()) + rangeRightBoundary;
            params.put("timeRange",timeRangeStr);
            callAPIAndWriteData(writer);
            System.out.println("Have got data: "+timeRangeStr);
            if(isLastRange){
                break;
            }else{
                beginTime=endTime;
            }
            
        }  
    }
    private void callAPIAndWriteData(BufferedWriter writer){
        RetArray2D retArray2D=new RetArray2D();
      //调用接口
        int   rst = client.callAPI_to_array2D(USER_ID, PASSWORD, interfaceId, params, retArray2D);
        if(retArray2D.request.errorCode!=0){
            System.out.println(retArray2D.request.errorMessage);
        }else{
            writeDataToFile(retArray2D,writer);
        }
    }
    
    private boolean initSuccess(){
        if(client !=null )
            return true;
        client = new DataQueryClient();
      //初始化接口服务连接资源
        try {
            client.initResources();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            client=null;
            e.printStackTrace(); 
            return false;
        }
        
    }
    
    private void destroyResources(){
      //释放接口服务连接资源
        if(client != null){
            client.destroyResources();
            client = null;
        }
    }
    
    private void writeDataToFile(RetArray2D ret,BufferedWriter writer){
        try{
            for(String[] line:ret.data){
                writer.write(line[0]);
                for(int i=1;i<line.length;i++)
                    writer.write(","+line[i]);
                writer.newLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
    public HashMap getParams() {
        return params;
    }
    public void setParams(HashMap params) {
        this.params = params;
    }

    public File getOutFile() {
        return outFile;
    }

    public void setOutFile(File outFile) {
        this.outFile = outFile;
    }
    
    
}
