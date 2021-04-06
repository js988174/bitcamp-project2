package com.eomcs.pms.dao.mariadb;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDaoImpl implements ProjectDao {

  SqlSession sqlSession;

  // Connection 객체를 자체적으로 생성하지 않고 외부에서 주입받는다.
  // - Connection 객체를 여러 DAO가 공유할 수 있다.
  // - 교체하기도 쉽다.
  public ProjectDaoImpl(SqlSession sqlSession) throws Exception {
    this.sqlSession = sqlSession;
  }

  @Override
  public int insert(Project project) throws Exception {
    return sqlSession.insert("ProjectMapper.insert", project);
  }

  @Override
  public List<Project> findAll() throws Exception {
    return sqlSession.selectList("ProjectMapper.findAll");
  }

  @Override
  public Project findByNo(int no) throws Exception {
    return sqlSession.selectOne("ProjectMapper.findByNo", no);
  }

  @Override
  public int update(Project project) throws Exception {
    return sqlSession.update("ProjectMapper.update", project);
  }

  @Override
  public int delete(int no) throws Exception {
    return sqlSession.delete("ProjectMapper.delete", no);
  }

  @Override
  public int insertMember(int projectNo, int memberNo) throws Exception {
    return sqlSession.insert("ProjectMapper.insertMember", projectNo);
  }

  @Override
  public List<Member> findAllMembers(int projectNo) throws Exception {
    return sqlSession.selectList("ProjectMapper.projectNo", projectNo);
  }

  @Override
  public int deleteMembers(int projectNo) throws Exception {
    return sqlSession.delete("ProjectMapper.deleteMembers", projectNo);
  }
}












