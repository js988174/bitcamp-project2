package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    p.setNo(Prompt.inputInt("번호? "));
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
            con.prepareStatement("insert into pms_project(no, title, content, sdt, edt, owner, members)"
                + " values(?,?,?,?,?,?,?)");) {

      stmt.setInt(1, p.getNo());
      stmt.setString(2, p.getTitle());
      stmt.setString(3, p.getContent());
      stmt.setDate(4, p.getStartDate());
      stmt.setDate(5, p.getEndDate());
      stmt.setString(6, p.getOwner());
      stmt.setString(7, p.getMembers());

      stmt.executeUpdate();

      System.out.println("프로젝트를 등록했습니다.");

    }
  }
}








