package test;
import java.io.PrintStream;
import java.util.HashMap;

import cma.cimiss.RetArray2D;
import cma.cimiss.client.DataQueryClient;
  
/*
 * 客户端调取，站点资料检索，返回RetArray2D对象
 */
public class StaElemSearchAPI_CLIB_callAPI_to_array2D {
    /*
     * main方法，程序入口
     * 如：按时间检索地面数据要素 getSurfEleByTime
     */
    public static void main(String[] args){
            /* 1. 定义client对象 */
            DataQueryClient   client = new DataQueryClient();
            /* 2. 调用方法的参数定义，并赋值 */
            /* 2.1 用户名&密码 */
            String userId = "NMIC_XTS_CloudWind";
            String pwd = "J19900128sl";
            /* 2.2 接口ID */
            String interfaceId = "getSurfEleByTimeRangeAndStaID";
            /* 2.3 接口参数，多个参数间无顺序 */
            HashMap params = new HashMap();
            //必选参数
            params.put("dataCode",   "SURF_CHN_MUL_DAY") ; //资料代码
            params.put("elements",   "Year,Mon,Day,TEM_Max,TEM_Min,PRE_Time_2020") ;//检索要素：站号、小时降水
            params.put("timeRange",   "[20150901000000,20151001000000]") ; //检索时间
            params.put("staIds","D1066" );
            //可选参数
           // params.put("orderby",   "Station_ID_C:ASC") ; //排序：按照站号从小到大
            params.put("limitCnt",   "10") ; //返回最多记录数：10
            /*   2.4 返回对象 */
            RetArray2D   retArray2D = new RetArray2D();
            /*   3. 调用接口 */
            try {
                 //初始化接口服务连接资源
                 client.initResources();
                 //调用接口
                 int   rst = client.callAPI_to_array2D(userId, pwd, interfaceId, params, retArray2D);
                 if(retArray2D.request.errorCode!=0){
                     System.out.println(retArray2D.request.errorMessage);
                 }else{
                     printRetArray2D(retArray2D);
                 }
                 System.out.println("Done!");
            } catch (Exception e) {
                 //异常输出 
                 e.printStackTrace();
            } finally {
                 //释放接口服务连接资源
                 client.destroyResources();
            }
    }
    private static void printRetArray2D(RetArray2D ret){
        PrintStream out=System.out;
        for(String[] line:ret.data){
            for(String field:line)
                out.print(field+"\t");
            out.println();
        }
    }
}