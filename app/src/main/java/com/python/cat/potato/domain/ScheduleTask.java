package com.python.cat.potato.domain;

import java.util.List;

/**
 * 待办事项
 */
public class ScheduleTask {

    /**
     * data : {"curPage":1,"datas":[{"completeDate":null,"completeDateStr":"","content":"这是一个待办事项？","date":1545926400000,"dateStr":"2018-12-28","id":5116,"priority":0,"status":0,"title":"你好吗","type":0,"userId":6649},{"completeDate":1544889600000,"completeDateStr":"2018-12-16","content":"","date":1544976000000,"dateStr":"2018-12-17","id":5121,"priority":0,"status":1,"title":"6666","type":0,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5120,"priority":0,"status":0,"title":"学习大大","type":2,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5124,"priority":0,"status":0,"title":"123qwe","type":0,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"真的吗？\n\n是的是的的\n\n是的是的山东省\n\n是的是的是的\n呜呜呜呜\n啊啊啊啊\n地对地导弹\n\n试试事实上\n啊啊啊咋咋\n\n擦擦擦擦车","date":1544889600000,"dateStr":"2018-12-16","id":5117,"priority":0,"status":0,"title":"今天是星期天鸭","type":0,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5118,"priority":0,"status":0,"title":"没有详情也是可以的哦","type":0,"userId":6649},{"completeDate":1544889600000,"completeDateStr":"2018-12-16","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5119,"priority":0,"status":1,"title":"生活是什么","type":3,"userId":6649}],"offset":0,"over":true,"pageCount":1,"size":20,"total":7}
     * errorCode : 0
     * errorMsg :
     */

    public DataBean data;
    public int errorCode;
    public String errorMsg;

    public static class DataBean {
        /**
         * curPage : 1
         * datas : [{"completeDate":null,"completeDateStr":"","content":"这是一个待办事项？","date":1545926400000,"dateStr":"2018-12-28","id":5116,"priority":0,"status":0,"title":"你好吗","type":0,"userId":6649},{"completeDate":1544889600000,"completeDateStr":"2018-12-16","content":"","date":1544976000000,"dateStr":"2018-12-17","id":5121,"priority":0,"status":1,"title":"6666","type":0,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5120,"priority":0,"status":0,"title":"学习大大","type":2,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5124,"priority":0,"status":0,"title":"123qwe","type":0,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"真的吗？\n\n是的是的的\n\n是的是的山东省\n\n是的是的是的\n呜呜呜呜\n啊啊啊啊\n地对地导弹\n\n试试事实上\n啊啊啊咋咋\n\n擦擦擦擦车","date":1544889600000,"dateStr":"2018-12-16","id":5117,"priority":0,"status":0,"title":"今天是星期天鸭","type":0,"userId":6649},{"completeDate":null,"completeDateStr":"","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5118,"priority":0,"status":0,"title":"没有详情也是可以的哦","type":0,"userId":6649},{"completeDate":1544889600000,"completeDateStr":"2018-12-16","content":"","date":1544889600000,"dateStr":"2018-12-16","id":5119,"priority":0,"status":1,"title":"生活是什么","type":3,"userId":6649}]
         * offset : 0
         * over : true
         * pageCount : 1
         * size : 20
         * total : 7
         */

        public int curPage;
        public int offset;
        public boolean over;
        public int pageCount;
        public int size;
        public int total;
        public List<TaskBean> datas;

        public static class TaskBean {
            /**
             * completeDate : null
             * completeDateStr :
             * content : 这是一个待办事项？
             * date : 1545926400000
             * dateStr : 2018-12-28
             * id : 5116
             * priority : 0
             * status : 0
             * title : 你好吗
             * type : 0
             * userId : 6649
             */

            public Object completeDate;
            public String completeDateStr;
            public String content;
            public long date;
            public String dateStr;
            public int id;
            public int priority;
            public int status;
            public String title;
            public int type;
            public int userId;
        }
    }
}
