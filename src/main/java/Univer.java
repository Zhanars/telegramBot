import com.vdurmont.emoji.EmojiParser;
import firstmenu.Configuration;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Univer {
    private static String countName = "";
    private static String userName = Configuration.getUniverUsername();
    private static String password = Configuration.getPass();
    private static String connectUrl = Configuration.getUniverHost();
    public static synchronized String IIN(String message) {
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT [students_sname], [students_name] FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + message + "%' and [student_edu_status_id] = 1";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = countName + rs.getString("students_sname");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }

    public static synchronized Boolean checkIIN(String IIN) {
        Boolean bool = false;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1 ";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                bool = true;
            } else {
                bool = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public static synchronized String getStOrPersonName(String IIN){
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            if (checkIINPersonalorStudent(IIN) == 1) {
                SQL = "SELECT [students_name] as name,[students_sname] as sname FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1  ";
            } else {
                SQL = "SELECT [personal_name] as name,[personal_sname] as sname FROM [atu_univer].[dbo].[univer_personal] WHERE [personal_identification_number] LIKE '%" + IIN + "%'";
            }
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("name");
                countName = countName + "','";
                countName = countName + rs.getString("sname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }


    public static synchronized int checkIINPersonalorStudent(String IIN) {
        int result = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL1 = "SELECT students_identify_code FROM [atu_univer].[dbo].[univer_students] WHERE [students_identify_code] LIKE '%" + IIN + "%' and [student_edu_status_id] = 1 ";
            ResultSet rs1 = stmt.executeQuery(SQL1);
            if (rs1.next()) {
                result = 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static synchronized String getAttendance(String IIN, String date3) {
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " ;with cte_tbl as" +
                    " (SELECT  [univer_subject].[subject_name_ru],[univer_subject].[subject_id],[univer_educ_type].[educ_type_name_ru] ," +
                    " [univer_attendance].[att_date], [univer_attendance].[ball], [univer_sheet_result].[date_keep],[univer_sheet_result].[result]," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id=49 then [univer_academ_calendar_pos].acpos_date_start else 0 end) r1," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id=55 then [univer_academ_calendar_pos].acpos_date_end else 0 end) r2," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id=56 then [univer_academ_calendar_pos].acpos_date_end else 0 end) r3," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id = 57  then acpos_date_start else 0 end) r4," +
                    " max(case" +
                    " when [univer_academ_calendar_pos].control_id = 57  then acpos_date_end else 0 end) r5," +
                    " max(case" +
                    " when [univer_sheet_result].[result] is null then 0 else [univer_sheet_result].[result] end) r6" +
                    " FROM [atu_univer].[dbo].[univer_attendance]" +
                    " left JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_attendance].[student_id]" +
                    " left JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                    " left JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " left JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                    " left JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                    " left JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " left JOIN [atu_univer].[dbo].[univer_sheet_result] ON [univer_sheet_result].[student_id] = [univer_students].[students_id]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 " +
                    " and [univer_academ_calendar_pos].[acpos_semester] = '"+semestr+"' " +
                    " and  [univer_academ_calendar_pos].[acpos_module] = [univer_educ_plan_pos].[acpos_module] " +
                    " and [univer_attendance].[ball]> 0 and [univer_attendance].[att_date] >= '" + date3 + "'" +
                    " GROUP BY [univer_subject].[subject_name_ru],[univer_subject].[subject_id],[univer_educ_type].[educ_type_name_ru] " +
                    " ,[univer_attendance].[att_date],[univer_attendance].[ball], [univer_sheet_result].[date_keep],[univer_sheet_result].[result]" +
                    ")" +
                    " select  cte_tbl.[subject_name_ru] as subname ,cte_tbl.[educ_type_name_ru] ," +
                    " Convert(varchar(10),CONVERT(date,cte_tbl.[att_date],106),103) as qwer, cte_tbl.[ball] as ball ," +
                    " max(case" +
                    " when cte_tbl.r1 <= cte_tbl.[att_date] and cte_tbl.r2 >= cte_tbl.[att_date] then '55' " +
                    " when cte_tbl.r2 < cte_tbl.[att_date] and cte_tbl.r3 >= cte_tbl.att_date then '56'" +
                    " end) r4" +
                    ", cte_tbl.[subject_id] as subject_name," +
                    "max(case" +
                    " when cte_tbl.r4 < cte_tbl.[date_keep] and cte_tbl.r5 >= cte_tbl.[date_keep] then cte_tbl.r6" +
                    " else 0" +
                    " end) as r8" +
                    " from cte_tbl" +
                    " GROUP BY cte_tbl.[subject_name_ru],cte_tbl.[educ_type_name_ru] , " +
                    " cte_tbl.[att_date], cte_tbl.[ball], cte_tbl.[subject_id]  " +
                    " ORder BY cte_tbl.[subject_name_ru]";
            ResultSet rs = stmt.executeQuery(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SQL;
    }

    public static synchronized String getAttendanceforweek(String IIN , String[][] Record) {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        ResultSet rs1 = null;
        ArrayList<String> Attendencerk = new ArrayList<String>();
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            Attendencerk.addAll(getSumAttendance(IIN));
            rs1 = stmt.executeQuery(getAttendance(IIN, date1));
            countName = "";
            String countName1 = "";
            int columns1 = 0;
            columns1 = rs1.getMetaData().getColumnCount();
            String rk1 = Attendencerk.get(Attendencerk.size()-3);
            String rk2 = Attendencerk.get(Attendencerk.size()-2);
            int rowCount = Record.length ;
            int colCount = Record[0].length;
            if (rs1 != null) {

                boolean bool=true;
                while (rs1.next()) {

                    if (Integer.parseInt(rs1.getString("r4")) == 55 && bool) {
                        bool = false;
                        System.out.println(Record[rowCount-1][1]);

                        // Сравнивает рк у последнего предмета
                        if(Record[rowCount-1][1] != "РК1") {
                            if (Integer.parseInt(rk1) <= Integer.parseInt(Record[rowCount - 1][1])) {
                                for (int i = 0; i < rowCount; i++) {

                                    countName = countName + Record[i][0] + " РК1:" + Record[i][1] + " РК2:" + Record[i][2] + " Экз:" + Record[i][3] + " Итог:" + Record[i][4] + "\n";

                                }
                                countName = countName + "\n Журнал посещений";
                            }
                        }else{
                            countName1 = "Сумма баллов по дисциплинам: \n";
                        for(String SumAttendecerk : Attendencerk){

                            countName = countName1 + SumAttendecerk + "\n";

                        }
                        countName = countName + "\n" +"Ваш текущий контроль РК1 \n";
                    }}else
                    if (Integer.parseInt(rs1.getString("r4")) == 56 && bool)
                        {


                        bool = false;
                            if(Integer.parseInt(rk2)<= Integer.parseInt(Record[rowCount-1][2])){
                                for (int i=0 ; i< rowCount; i++){

                                        countName = countName + Record[i][0] + " РК1:" + Record[i][1] + " РК2:" + Record[i][2] + " Экз:"+ Record[i][3] + " Итог:"+ Record[i][4] + "\n";

                                }
                                countName = countName + "\n Журнал посещений";
                            }else{
                                countName1 = "Сумма баллов по дисциплинам \n";
                            for(String SumAttendecerk : Attendencerk){
                                countName =countName1+ SumAttendecerk + "\n";

                            }}
                            countName = countName + "\n" +"Ваш текущий контроль РК2 \n";
                    }

                        countName = countName + " Дисциплина: " + rs1.getString("subname") + " (" + rs1.getString("educ_type_name_ru")+" ). Дата: " +
                        rs1.getString("qwer") + ". Балл: "  + rs1.getString("ball") ;

                    countName = countName + "\n";

                }
            } else {
                countName = "123456";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }

    public static synchronized ArrayList<String> getSumAttendance(String IIN) {

        ResultSet rs1 = null;
        int sumrk1 = -1, sumrk2 = -1, i = 1;
        int subject_name = 0;
        ArrayList<String> SumAttendance = new ArrayList<String>();
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            rs1 = stmt.executeQuery(getAttendance(IIN, getStartDate(IIN)));
            countName = "";
            String ekz = "";

            if (rs1 != null) {
                while (rs1.next()) {
                    if (subject_name != Integer.parseInt(rs1.getString("subject_name"))) {
                        if(sumrk1!=-1) {


                            countName = countName + " РК1: " + Integer.toString(sumrk1) + " РК2: " + Integer.toString(sumrk2) + " Экз: " + rs1.getString("r8") +"\n"+ rs1.getString("subname");
                            SumAttendance.add(countName);
                        } else {

                            countName = rs1.getString("subname");
                        }
                        subject_name = Integer.parseInt(rs1.getString("subject_name"));
                        i = i +1;
                        sumrk1 = 0;
                        sumrk2 = 0;
                    }
                    if (subject_name == Integer.parseInt(rs1.getString("subject_name"))){
                        String ball = rs1.getString("ball");
                        int len = ball.length();
                        ball = ball.substring(0,len-2);
                        if (Integer.parseInt(rs1.getString("r4")) == 55) {
                            sumrk1 = sumrk1 + Integer.parseInt(ball);
                        } else if (Integer.parseInt(rs1.getString("r4"))== 56) {
                            sumrk2 = sumrk2 + Integer.parseInt(ball);
                        }
                    }
                    ekz = rs1.getString("r8");
                }
                SumAttendance.add(Integer.toString(sumrk1));
                SumAttendance.add(Integer.toString(sumrk2));
                countName = countName + " РК1: " + Integer.toString(sumrk1) + " РК2: " + Integer.toString(sumrk2) + " Экз: " + ekz;
                SumAttendance.add(countName);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  SumAttendance;
    }

    public static synchronized String[][] getProgressforAttendence(String IIN){
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL =  " ;with tableforresult as " +
                    " (SELECT [univer_progress].[subject_name_ru]" +
                    "  ,max(case" +
                    " when ([controll_type_id] >= 2 and [controll_type_id]<= 35) or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "  else [univer_progress].[progress_result_rk1] end ) RK1" +
                    "  ,max(case" +
                    "  when ([controll_type_id] >= 2 and [controll_type_id]<= 35) or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "  when [controll_type_id] = 48 or [controll_type_id] = 49 then [progress_result_rk1]" +
                    "  else [univer_progress].[progress_result_rk2] end ) RK2" +
                    "  ,[univer_progress].[progress_result]" +
                    "  ,[univer_progress].[progress_result_rk2]" +
                    "  ,[univer_progress].[progress_result_rk1]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    "  WHERE [univer_students].[students_identify_code] LIKE '"+IIN+"' and [univer_progress].[status] = 1 and [univer_progress].[n_seme] = '"+semestr+"'" +
                    "  GROUP BY [univer_progress].[subject_name_ru]" +
                    "  ,[progress_result_rk1]" +
                    "  ,[univer_progress].[progress_result_rk2]" +
                    "  ,[univer_progress].[progress_result]" +
                    " )" +
                    " SELECT tableforresult.[subject_name_ru] as subject" +
                    "           ,tableforresult.RK1 as rk1" +
                    "   ,tableforresult.RK2 as rk2" +
                    "  ,tableforresult.[progress_result] as result" +
                    "  ,ROUND((0.3*tableforresult.RK1 + 0.3*tableforresult.RK2+0.4*tableforresult.[progress_result]),0) as resultekz" +
                    "  FROM tableforresult" +
                    " ORDER BY tableforresult.[subject_name_ru]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount+1][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            result[0][0] = "Дисциплина";
            result[0][1] = "РК1";
            result[0][2] = "РК2";
            result[0][3] = "Экз";
            result[0][4] = "Итог";
            int j = 1;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0][0];
        }
    }





    public static synchronized String getStartDate(String IIN) {
        String SQL = "";
        String semestr = getSemestr(IIN);
        Calendar c = new GregorianCalendar();
        String date2 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        c.add(Calendar.DAY_OF_YEAR, -7);
        String date1 = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT TOP 1 Convert(varchar(10),CONVERT(date,[univer_academ_calendar_pos].[acpos_date_start],106),103) as start" +
                    " FROM [atu_univer].[dbo].[univer_attendance]" +
                    " JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_attendance].[student_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_attendance].[group_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_educ_type] ON [univer_educ_type].[educ_type_id] = [univer_group].[educ_type_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_academ_calendar_pos] ON [univer_academ_calendar_pos].[educ_plan_id] = [univer_educ_plan_pos].[educ_plan_id]" +
                    " JOIN  [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 and [univer_academ_calendar_pos].[acpos_semester] = '"+semestr+"'" +
                    " and  [univer_academ_calendar_pos].[acpos_module] = [univer_educ_plan_pos].[acpos_module] and [univer_attendance].[ball]>= 0 and [univer_academ_calendar_pos].control_id = 49" +
                    " and [univer_attendance].[att_date] > '" + date1 + "'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    countName = rs.getString("start");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countName;
    }





    public static synchronized String[][] getTranskript(String IIN){
        String SQL = "";
        String SQL1 = "";

        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT CONCAT ([univer_students].[students_sname],' ',[univer_students].[students_name],' ',[univer_students].[students_father_name]) as fio" +
                    "   ,[univer_faculty].[faculty_name_ru]" +
                    "   ,[univer_speciality].[speciality_name_ru]" +
                    "   ,[univer_students].[students_curce_number]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1";

            SQL1 = " ;with tableforresult as " +
                    " (SELECT [univer_progress].[subject_name_ru]" +
                    "      ,[univer_progress].[progress_credit]" +
                    "      ,max(case" +
                    "       when [controll_type_id] >= 2 and [controll_type_id]<= 35 or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "       else [univer_progress].[progress_result_rk1] end ) RK1" +
                    "       ,max(case\n" +
                    "       when [controll_type_id] >= 2 and [controll_type_id]<= 35 or [controll_type_id] = 50  then [univer_progress].[progress_result]" +
                    "       when [controll_type_id] = 48 or [controll_type_id] = 49 then [progress_result_rk1]" +
                    "       else [univer_progress].[progress_result_rk2] end ) RK2" +
                    "      ,[univer_mark_type].[mark_type_symbol]" +
                    "      ,[univer_mark_type].[mark_type_gpa]" +
                    "      ,[univer_progress].[n_seme]" +
                    "      ,[univer_progress].[academ_year]" +
                    "      ,[univer_progress].[progress_result]" +
                    "      ,[univer_progress].[progress_result_rk2]" +
                    "      ,[univer_progress].[progress_result_rk1]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_faculty] ON [univer_faculty].[faculty_id] = [univer_students].faculty_id" +
                    "  JOIN [atu_univer].[dbo].[univer_speciality] ON [univer_speciality].[speciality_id] = [univer_students].[speciality_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    "  WHERE [univer_students].[students_identify_code] LIKE '"+IIN+"' and [univer_progress].[status] = 1" +
                    "  GROUP BY [univer_progress].[subject_name_ru]" +
                    "      ,[univer_progress].[progress_credit]" +
                    "      ,[univer_mark_type].[mark_type_symbol]" +
                    "      ,[univer_mark_type].[mark_type_gpa]" +
                    "      ,[univer_progress].[n_seme]" +
                    "      ,[univer_progress].[academ_year]" +
                    "      ,[progress_result_rk1]" +
                    "      ,[univer_progress].[progress_result_rk2]" +
                    "      ,[univer_progress].[progress_result])" +
                    "      SELECT tableforresult.[subject_name_ru]" +
                    "      ,tableforresult.[progress_credit]" +
                    "      ,ROUND(((0.6*((tableforresult.RK1 + tableforresult.RK2)/2))+ ((tableforresult.[progress_result])*0.4)),0) as resultekz" +
                    "      ,tableforresult.[mark_type_symbol]" +
                    "      ,tableforresult.[mark_type_gpa]" +
                    "      ,tableforresult.[n_seme]" +
                    "      ,tableforresult.[academ_year]" +
                    "      FROM tableforresult" +
                    "      ORDER BY tableforresult.[academ_year]";
            ResultSet rs = stmt.executeQuery(SQL1);
            int rowCount = getRowCount(rs);
            int colCount = rs.getMetaData().getColumnCount();
            rs.close();
            String[][] result = new String[rowCount + 2][colCount];
            ResultSet rs1 = stmt.executeQuery(SQL);
            while (rs1.next()) {
                result[0][0] = rs1.getString("fio");
                result[0][1] = rs1.getString("faculty_name_ru");
                result[0][2] = rs1.getString("speciality_name_ru");
                result[0][3] = rs1.getString("students_curce_number");
            }
            result[1][0] = "Дисциплина";
            result[1][1] = "Кол-во кредитов";
            result[1][2] = "Оценка";
            result[1][3] = "Оценка по буквенной системе";
            result[1][4] = "Цифровой эквивалент";
            result[1][5] = "Семестр";
            rs1.close();
            ResultSet rs2 = stmt.executeQuery(SQL1);
            int i = 2;
            while (rs2.next()) {
                for (int j = 0; j < colCount; j++) {
                    result[i][j] = rs2.getString(j+1);
                }
                i++;
            }
            return  result;

        } catch (SQLException e) {
            e.printStackTrace();
            return  result1;
        }
    }

    public  static  String[][] getSchedule(String IIN){
        String SQL = "";
        String SQL1 = "";
        String semestr = getSemestr(IIN);

        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = "SELECT  [schedule_time_id]" +
                    "      ,Concat([schedule_time_begin],'-'" +
                    "      ,[schedule_time_end]) as timesch" +
                    "  FROM [atu_univer].[dbo].[univer_schedule_time]" +
                    "  where [schedule_time_type_id] = 1 and [status] =1" +
                    "  order by timesch";

            SQL1 = "SELECT subj.subject_name_ru" +
                    "      ,et.educ_type_name_ru" +
                    "  ,substring(Concat(per.personal_sname, ' ' " +
                    "  ,per.personal_name, ' '" +
                    "  ,per.personal_father_name), 1, charindex(' ', Concat(per.personal_sname, ' ' " +
                    "  ,per.personal_name, ' '" +
                    "  ,per.personal_father_name)))" +
                    " +substring(Concat(per.personal_sname, ' ' " +
                    "  ,per.personal_name, ' '" +
                    "  ,per.personal_father_name), charindex(' ', Concat(per.personal_sname, ' ' " +
                    "  ,per.personal_name, ' '" +
                    "  ,per.personal_father_name))+1,1)+'.'" +
                    "  +substring(Concat(per.personal_sname, ' ' ,per.personal_name, ' ',per.personal_father_name), charindex(' ', Concat(per.personal_sname, ' ' " +
                    "  ,per.personal_name, ' '" +
                    "   ,per.personal_father_name), charindex(' ', Concat(per.personal_sname, ' ' " +
                    "   ,per.personal_name, ' '" +
                    "   ,per.personal_father_name))+1)+1,1)+'.' as fio" +
                    "  ,Concat(b.building_name_ru,' ',a.audience_number_ru) as auditoria" +
                    "      ,scht.schedule_time_id" +
                    "      ,[schedule_week_day]" +
                    "  FROM [atu_univer].[dbo].[univer_schedule] sc" +
                    "  join [univer_group]  g on g.group_id = sc.group_id" +
                    "  join [univer_educ_plan_pos] eps on eps.educ_plan_pos_id = g.educ_plan_pos_id" +
                    "  join univer_group_student gs on gs.group_id = g.group_id" +
                    "  join univer_educ_type et on et.educ_type_id = g.educ_type_id" +
                    "  join univer_subject subj on subj.subject_id = eps.subject_id" +
                    "  left join univer_teacher tc on tc.teacher_id = g.teacher_id" +
                    "  left join univer_personal per on per.personal_id = tc.personal_id" +
                    "  join univer_students  s on s.students_id = gs.student_id" +
                    "  left  join univer_schedule_time scht on scht.schedule_time_id = sc.schedule_time_id" +
                    "  left join univer_schedule_time_type stt on stt.schedule_time_type_id = scht.schedule_time_type_id" +
                    "  left join univer_audience a on a.audience_id = sc.audience_id" +
                    "   left join univer_building b on b.building_id = a.building_id" +
                    "    where s.[students_identify_code] LIKE '" + IIN + "' and eps.educ_plan_pos_semestr = '"+semestr+"'  ";
            ResultSet rs = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs)+1;
            int colCount = 6;
            int[] schid = new int[rowCount] ;
            String[][] result = new String[rowCount][colCount];
            int i = 1;
            rs.close();
            rs = stmt.executeQuery(SQL);
            while (rs.next()){
                schid[i] = Integer.parseInt(rs.getString("schedule_time_id"));
                result[i][0] =rs.getString("timesch");
                i++;
            }
            rs.close();
            result[0][0] = "";
            result[0][1] = "Пн";
            result[0][2] = "Вт";
            result[0][3] = "Ср";
            result[0][4] = "Чт";
            result[0][5] = "Пт";
            ResultSet rs1 = stmt.executeQuery(SQL1);
            int maxrow=0;
            while (rs1.next()) {
                int row = 1;
                while (schid[row] != Integer.parseInt(rs1.getString("schedule_time_id"))){
                    row++;
                }
                if (row > maxrow) maxrow = row;
                result[row][Integer.parseInt(rs1.getString("schedule_week_day"))] = rs1.getString("subject_name_ru") + "( "
                        + rs1.getString("educ_type_name_ru") + ")\n"
                        + rs1.getString("fio") + "\n"
                        + rs1.getString("auditoria");
            }
            result[0][0] = Integer.toString(maxrow);
          return  result;


        } catch (SQLException e) {
            e.printStackTrace();
            return  result1;

        }
    }


    public static synchronized String[][] getExamSchedule(String IIN){
        Calendar c = new GregorianCalendar();
        String nowDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        c.add(Calendar.MONTH, -1);
        String takeMonth = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        c.add(Calendar.MONTH, 1);
        String addMonth = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        String SQL = "";
        String[][] result1 = new String[1][1];
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT  [subject_name_ru]" +
                    " ,CONCAT([personal_sname] ,' '" +
                    "       ,[personal_name],' '" +
                    "       ,[personal_father_name]) as FIO" +
                    " ,[building_name_ru]" +
                    " ,[univer_educ_plan_pos].[exam_form_id]" +
                    " ,[exam_time]" +
                    ", [audience_number_ru]" +
                    " FROM [atu_univer].[dbo].[univer_exam_schedule]" +
                    " JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].[group_id] = [univer_exam_schedule].[group_id]" +
                    " JOIN [atu_univer].[dbo].[univer_group] ON [univer_group].[group_id] = [univer_exam_schedule].[group_id]" +
                    " JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].[educ_plan_pos_id] = [univer_group].[educ_plan_pos_id]" +
                    " JOIN [atu_univer].[dbo].[univer_subject] ON [univer_subject].[subject_id] = [univer_educ_plan_pos].[subject_id]" +
                    " JOIN [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] = [univer_group_student].[student_id]" +
                    " JOIN [atu_univer].[dbo].[univer_teacher] ON [univer_teacher].teacher_id = [univer_exam_schedule].examiner_teacher_id" +
                    " JOIN [atu_univer].[dbo].[univer_personal] ON [univer_personal].personal_id = [univer_teacher].personal_id " +
                    " JOIN [atu_univer].[dbo].[univer_audience] ON [univer_audience].audience_id = [univer_exam_schedule].audience_id" +
                    " JOIN [atu_univer].[dbo].[univer_building] ON [univer_building].building_id = [univer_audience].building_id" +
                    " Where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1 and " +
                    " [univer_exam_schedule].[exam_time] > '"+takeMonth+"'" +
                    " ORDER BY [exam_time]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount + 1][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            result[0][0] = "Дисциплина";
            result[0][1] = "ФИО Преподавателя";
            result[0][2] = "Аудитория";
            result[0][3] = "Тип";
            int j = 1;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    if (rs2.getString(i + 1).equals("1") ) {
                        result[j][i] = "Устный";
                    }else if (rs2.getString(i + 1).equals("0")){
                        result[j][i] = "Письменный";
                    }
                    else {
                        if(i == 2)
                            result[j][i] = rs2.getString(i + 1) + " | Ауд " + rs2.getString(i + 4);
                        else
                        result[j][i] = rs2.getString(i + 1);

                    }
                }
                j++;


            }
            return  result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  result1;

    }

    public static synchronized String[][] getAcademcal(String IIN) {
        String SQL = "";
        String dataStart = getStartDate(IIN);
        String[][] result1 = new String[1][1];

        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT [univer_control].[control_name_ru]" +
                    "       ,[acpos_semester]" +
                    "       ,Convert(varchar(10),CONVERT(date,[acpos_date_start],106),103) as qwer" +
                    "       ,Convert(varchar(10),CONVERT(date,[acpos_date_end],106),103) as qwer1" +
                    "  FROM [atu_univer].[dbo].[univer_academ_calendar_pos]" +
                    "  JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].educ_plan_id = [univer_academ_calendar_pos].educ_plan_id" +
                    "  JOIN [atu_univer].[dbo].[univer_group] ON [univer_group].educ_plan_pos_id = [univer_educ_plan_pos].educ_plan_pos_id" +
                    "  JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].group_id = [univer_group].group_id" +
                    "  JOIN [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] = [univer_group_student].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_control] ON [univer_control].control_id = [univer_academ_calendar_pos].control_id" +
                    "  where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1  " +
                    " and acpos_semester >= '"+getSemestr(IIN)+"' and [univer_academ_calendar_pos].acpos_module = 0" +
                    "  GROUP BY " +
                    "       [acpos_semester]" +
                    "      ,[univer_control].[control_name_ru]" +
                    "      ,[acpos_date_start]" +
                    "      ,[acpos_date_end]" +
                    "ORDER BY [acpos_semester], [acpos_date_start]";
            ResultSet rs1 = stmt.executeQuery(SQL);

            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount + 1][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            result[0][0] = "Контроль";
            result[0][1] = "Семестр";
            result[0][2] = "Дата начала";
            result[0][3] = "Дата конца";
            int j = 1;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                            result[j][i] = rs2.getString(i + 1);
                    }
                j++;
                }

            return  result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0][0];
        }
    }

    public static synchronized String getAdvicer(String IIN){
        countName = "Ваш эдвайзер \n";
        String SQL = "";
        int i = 1;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT CONCAT([personal_sname] , ' '" +
                    "      ,[personal_name], ' '" +
                    "      ,[personal_father_name]) as fio" +
                    "      ,[personal_email]" +
                    "      ,[personal_mobile_phone]" +
                    "      ,[personal_home_phone]" +
                    "  FROM [atu_univer].[dbo].[univer_advicer]" +
                    "  JOIN [atu_univer].[dbo].[univer_advicer_student_link] ON [univer_advicer_student_link].advicer_id = [univer_advicer].advicer_id" +
                    "  JOIN [atu_univer].[dbo].[univer_personal] ON [univer_personal].personal_id = [univer_advicer].personal_id" +
                    "  JOIN [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] = [univer_advicer_student_link].[student_id]" +
                    "   where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_students].[student_edu_status_id] = 1";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                    countName = countName +" "+ rs.getString("fio") + "\n"+EmojiParser.parseToUnicode(":e-mail: ") + rs.getString("personal_email")
                            + EmojiParser.parseToUnicode("\n:telephone_receiver: ") + rs.getString("personal_mobile_phone")
                    +  EmojiParser.parseToUnicode("\n:phone: ")+ rs.getString("personal_home_phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  countName;
    }

    public static synchronized String[][] getSubject(String IIN) {
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = "SELECT DISTINCT [subject_name_ru]" +
                    "   ,[univer_educ_plan_pos].subject_id" +
                    "  FROM [atu_univer].[dbo].[univer_group]" +
                    "  JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].group_id = [univer_group].group_id" +
                    "  JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].educ_plan_pos_id = [univer_group].educ_plan_pos_id" +
                    "  JOIN [atu_univer].[dbo].[univer_subject] ON [univer_subject].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  JOIN univer_students ON univer_students.students_id = [univer_group_student].student_id" +
                    "  JOIN [atu_univer].[dbo].[univer_teacher_file] ON [univer_teacher_file].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [educ_plan_pos_semestr] = '"+semestr+"' ORDER BY [subject_name_ru]";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0][0];
        }
    }
    public static synchronized String[][] getTeachers(String IIN, String SubjectId) {
        String SQL = "";
        String semestr = getSemestr(IIN);
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = "SELECT DISTINCT" +
                    "       CONCAT([personal_sname] , ' '" +
                    "      ,[personal_name], ' '" +
                    "      ,[personal_father_name]) as fio" +
                    "      , [univer_group].teacher_id" +
                    "      ,[univer_educ_plan_pos].subject_id" +
                    "  FROM [atu_univer].[dbo].[univer_group]" +
                    "  JOIN [atu_univer].[dbo].[univer_group_student] ON [univer_group_student].group_id = [univer_group].group_id" +
                    "  JOIN [atu_univer].[dbo].[univer_educ_plan_pos] ON [univer_educ_plan_pos].educ_plan_pos_id = [univer_group].educ_plan_pos_id" +
                    "  JOIN [atu_univer].[dbo].[univer_subject] ON [univer_subject].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  JOIN univer_students ON univer_students.students_id = [univer_group_student].student_id" +
                    "  JOIN [atu_univer].[dbo].[univer_teacher_file] ON [univer_teacher_file].teacher_id = [univer_group].teacher_id" +
                    "  JOIN [univer_teacher] ON [univer_teacher].teacher_id = [univer_teacher_file].teacher_id" +
                    "  JOIN [univer_personal] ON [univer_personal].personal_id = [univer_teacher].personal_id" +
                    "  where [univer_students].[students_identify_code] LIKE '" + IIN + "' and [educ_plan_pos_semestr] = '"+semestr+"' and  [univer_teacher_file].subject_id = [univer_educ_plan_pos].subject_id" +
                    "  and [univer_educ_plan_pos].subject_id = '"+SubjectId+"' ORDER BY [univer_group].teacher_id";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0][0];
        }
    }
    public static synchronized String[][] getFiles(String TeacherId, String SubjectId) {
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT [teacher_file_title]" +
                    "      ,[teacher_file_name]" +
                    "       ,[teacher_id]"+
                    "  FROM [atu_univer].[dbo].[univer_teacher_file] where [teacher_id] = '"+TeacherId+"' and [subject_id] = '"+SubjectId+"' ";
            ResultSet rs1 = stmt.executeQuery(SQL);
            int rowCount = getRowCount(rs1);
            int colCount = rs1.getMetaData().getColumnCount();
            rs1.close();
            String[][] result = new String[rowCount][colCount];
            ResultSet rs2 = stmt.executeQuery(SQL);
            int j = 0;
            while (rs2.next()) {
                for (int i = 0; i < colCount; i++) {
                    result[j][i] = rs2.getString(i + 1);
                }
                j++;
            }

            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0][0];
        }
    }

    public static synchronized String resetPassword(String IIN){
        countName = "";
        String SQL = "";
        String SQL1 = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
        SQL = " SELECT [univer_users].[user_id] as Userst" +
                "  FROM [atu_univer].[dbo].[univer_users]" +
                "  JOIN univer_students ON univer_students.[user_id] = [univer_users].[user_id]" +
                " where [univer_students].[students_identify_code] LIKE '" + IIN + "' ";
        ResultSet rs = stmt.executeQuery(SQL);
        while (rs.next()){
            countName = rs.getString("Userst");
        }
        rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {

            SQL1 = " UPDATE [atu_univer].[dbo].[univer_users] SET user_password = 'A9CA322AC0D07067D41A65860B2FFE41B5E70B1A5450FF7F16B8A26DB72099CD49F549DFBF7F269A909852920E98995BDE11CE1A8D7CD65F51C4674ADA296096'" +
                    "where user_id = '"+countName+"'";
            ResultSet rs1 = stmt.executeQuery(SQL1);
            rs1.close();
            countName = "Пароль сброшен";

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countName;
    }



    public static synchronized Double getGPA(String IIN){
        int creditSum = 0;
        double result = 0 , summa = 0;
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = " SELECT " +
                    "      ,[univer_progress].[progress_credit]" +
                    "      ,[univer_mark_type].[mark_type_gpa]" +
                    "  FROM [atu_univer].[dbo].[univer_progress]" +
                    "  JOIN  [atu_univer].[dbo].[univer_students] ON [univer_students].[students_id] =[univer_progress].[student_id]" +
                    "  JOIN [atu_univer].[dbo].[univer_mark_type] ON [univer_mark_type].[mark_type_id] = [univer_progress].mark_type_id" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' and [univer_progress].[status] = 1" +
                    " ORDER BY  [univer_progress].[academ_year]";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    int credit = Integer.parseInt(rs.getString("progress_credit"));
                    creditSum = creditSum + credit;
                    summa = summa + Double.parseDouble(rs.getString("mark_type_gpa"))*credit;
                }
                result = summa / creditSum;
            } else {
                result = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public  static String getSemestr(String IIN){

        countName = "";
        String SQL = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            SQL = " SELECT top 1" +
                    "   [students_curce_number] as curse" +
                    "  FROM [atu_univer].[dbo].[univer_students]" +
                    " WHERE [univer_students].[students_identify_code] LIKE '" + IIN + "' " +
                    " ORDER BY curse desc";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                countName = rs.getString("curse");
            }

            int kurs = Integer.parseInt(countName);
            int i = 2 * (kurs - 1);
            Calendar today = new GregorianCalendar();
            Calendar startDate = new GregorianCalendar();
            Calendar endDate = new GregorianCalendar();
            endDate.set(today.get(Calendar.YEAR), 0,20);
            if (today.before(endDate)) {
                startDate.set(today.get(Calendar.YEAR)-1, 7, 28);
                endDate.set(today.get(Calendar.YEAR), 0, 20);
            } else {
                startDate.set(today.get(Calendar.YEAR), 7, 28);
                endDate.set(today.get(Calendar.YEAR) + 1, 0, 20);
            }
            if (today.after(startDate) && today.before(endDate)){
                i += 1;

            }
            startDate.set(today.get(Calendar.YEAR), 0,21);
            endDate.set(today.get(Calendar.YEAR), 5,1);
            if (today.after(startDate) && today.before(endDate)){
                i += 2;

            }


            countName = Integer.toString(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countName;
    }

    private static int getRowCount(ResultSet resultSet) {
        int count = 0;
        if (resultSet == null) {
            return 0;
        }
        try {
            while (resultSet.next()){
                count++;
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {

        }
        return count;
    }
    public static synchronized String getEmail(String IIN){
        String result = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password); Statement stmt = conn.createStatement();) {
            String SQL = "SELECT [students_email]" +
                    "  FROM [atu_univer].[dbo].[univer_students] where [univer_students].[students_identify_code] LIKE '"+IIN+"'";
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getString("students_email");
                }
            } else {
                result = "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }
    public static synchronized String getSpeciality(String IIN){
        String SQL = "", result = "";
        try (Connection conn = DriverManager.getConnection(connectUrl, userName, password);
             Statement stmt = conn.createStatement()) {
            SQL = "SELECT sp.speciality_okpd" +
                    "  FROM [atu_univer].[dbo].[univer_students] st" +
                    "  left join univer_speciality sp on sp.speciality_id = st.speciality_id" +
                    "  where students_identify_code = '" + IIN +"'";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                result = rs.getString("speciality_okpd");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
