package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectAddHandler implements Command {

  MemberValidatorHandler memberValidator;

  public ProjectAddHandler( MemberValidatorHandler memberValidator) {
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception{

    System.out.println("[프로젝트 등록]");

    Project p = new Project();


    p.setTitle(Prompt.inputString("프로젝트명? "));
    p.setContent(Prompt.inputString("내용? "));
    p.setStartDate(Prompt.inputDate("시작일? "));
    p.setEndDate(Prompt.inputDate("종료일? "));

    p.setOwner(memberValidator.inputMember("만든이?(취소: 빈 문자열) "));
    if (p.getOwner() == null) {
      System.out.println("프로젝트 입력을 취소합니다.");
      return;
    }

    p.setMembers(memberValidator.inputMembers("팀원?(완료: 빈 문자열) "));

    try (Connection con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt =
            con.prepareStatement("insert into pms_project(title, content, sdt, edt, owner)"
                + " values(?,?,?,?,?)", 
                Statement.RETURN_GENERATED_KEYS);
        PreparedStatement stmt2 = con.prepareStatement(
            "insert into pms_member_project(member_no,project_no) values(?,?)")) {

      // 수동 커밋 설정한다.
      // -pms_project 테이블과 pms_member_project테이블에 모두 성공적으로 데이터를 입력하고,
      // 데이터 작업을 완료한다.
      con.setAutoCommit(false);

      stmt.setString(1, p.getTitle());
      stmt.setString(2, p.getContent());
      stmt.setDate(3, p.getStartDate());
      stmt.setDate(4, p.getEndDate());
      stmt.setInt(5, p.getOwner().getNo());      
      stmt.executeUpdate();

      try (ResultSet keyRs = stmt.getGeneratedKeys()) {
        keyRs.next();
        p.setNo(keyRs.getInt(1));
      }

      // 2) 프로젝트 팀원들을 추가한다.
      for (Member member : p.getMembers()) {
        stmt2.setInt(1, member.getNo());
        stmt2.setInt(2 ,p.getNo());
        stmt2.executeUpdate();
      }

      con.commit();
      System.out.println("프로젝트를 등록했습니다.");

    }
  }

}








