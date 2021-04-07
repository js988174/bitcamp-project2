package com.eomcs.pms.dao.mariadb;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Task;

public class TaskDaoImpl implements TaskDao {

  SqlSession sqlSession;

  // Connection 객체를 자체적으로 생성하지 않고 외부에서 주입받는다.
  // - Connection 객체를 여러 DAO가 공유할 수 있다.
  // - 교체하기도 쉽다.
  public TaskDaoImpl(SqlSession sqlSession) throws Exception {
    this.sqlSession = sqlSession;
  }

  @Override
  public int insert(Task task) throws Exception {
    return sqlSession.insert("TaskMapper.insert", task);
  }


  @Override
  public List<Task> findAll() throws Exception {
    return sqlSession.selectList("TaskMapper.findAll");
  }

  @Override
  public Task findByNo(int no) throws Exception {
    return sqlSession.selectOne("TaskMapper.findByNo", no);
  }


  @Override
  public int update(Task task) throws Exception {
    return sqlSession.update("TaskMapper.update", task);
  }

  @Override
  public int delete(int no) throws Exception {
    return sqlSession.delete("TaskMapper.delete", no);
  }

  @Override
  public List<Task> findByProjectNo(int projectNo) throws Exception {
    return sqlSession.selectList("TaskMapper.findByProjectNo", projectNo);
  }


  @Override
  public int deleteByProjectNo(int projectNo) throws Exception {
    return sqlSession.delete("TaskMapper.deleteByProjectNo", projectNo);   
  }
}












