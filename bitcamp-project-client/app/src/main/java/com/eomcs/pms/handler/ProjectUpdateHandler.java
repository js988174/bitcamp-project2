package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectUpdateHandler implements Command {

  MemberValidatorHandler memberValidator;

  public ProjectUpdateHandler( MemberValidatorHandler memberValidator) {
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception{

    System.out.println("[프로젝트 변경]");

    int no = Prompt.inputInt("번호? ");




    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "select"
                + "p.no,"
                + "p.title,"
                + "p.content"
                + "p.sdt,"
                + "p.edt,"
                + "m.no as owner_no,"
                + "m.name as owner_name"
                + "from pms_project p"
                + "inner join pms_member m on p.owner=m.no"
                + "where p.no=?");
        PreparedStatement stmt2 = con.prepareStatement(
            " select"
                + " m.no,"
                + " m.name"
                + " from pms_member_project mp"
                + " inner join pms_member m on mp.member_no=m.no"
                +  " where"
                +  " mp.project_no=?");
        PreparedStatement stmt3 = con.prepareStatement(
            "update pms_member_project set"
                + " title=?,"
                + "content=?,"
                + "sdt=?,"
                + "edt=?,"
                + "owner=?"
                + "where no=?");
        PreparedStatement stmt4 = con.prepareStatement(
            "delete from pms_member_project where project_no=?");
        PreparedStatement stmt5 = con.prepareStatement(
            "insert into pms_member_project where project_no=?")) {


      con.setAutoCommit(false);
      Project p = new Project();

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 프로젝트가 없습니다.");
          return;
        }

        p.setNo(no);


        p.setTitle(Prompt.inputString(String.format("제목(%s)? ", rs.getString("title"))));
        p.setContent(Prompt.inputString(String.format("내용(%s)? ", rs.getString("content"))));
        p.setStartDate(Prompt.inputDate(String.format("시작일(%s)? ", rs.getDate("sdt"))));
        p.setEndDate(Prompt.inputDate(String.format("종료일(%s)? ", rs.getDate("edt"))));

        p.setOwner(memberValidator.inputMember(String.format("만든이(%s)?"
            + "(취소: 빈 문자열) ", rs.getString("owner_name"))));

        if (p.getOwner() == null) {
          System.out.println("프로젝트 변경을 취소하였습니다.");
          return;
        }


        StringBuilder strings = new StringBuilder();
        try (ResultSet keyRs = stmt.getGeneratedKeys()) {
          keyRs.next();
          p.setNo(keyRs.getInt(1));
        }

        p.setMembers(memberValidator.inputMembers(
            String.format("팀원(%s)?(완료: 빈 문자열) ",strings)));

        String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");

        if (!input.equalsIgnoreCase("Y")) {
          System.out.println("프로젝트 변경을 취소하였습니다.");
          return;
        }



        stmt3.setString(1, p.getTitle());
        stmt3.setString(2, p.getContent());
        stmt3.setDate(3, p.getStartDate());
        stmt3.setDate(4, p.getEndDate());
        stmt3.setInt(5,p.getOwner().getNo());
        stmt3.setInt(6, p.getNo());
        stmt3.executeUpdate();


        stmt4.setInt(1, p.getNo());
        stmt4.executeUpdate();

        for (Member member : p.getMembers()) {
          stmt5.setInt(1, member.getNo());
          stmt5.setInt(2, no);
          stmt5.executeUpdate();
        }

        con.commit();
        System.out.println("프로젝트을 변경하였습니다.");



      }
    }
  }
}


