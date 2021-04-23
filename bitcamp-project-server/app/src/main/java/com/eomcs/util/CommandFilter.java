package com.eomcs.util;

import com.eomcs.pms.handler.Command;

// FilterChain 구현체가 실행하는 필터이다.
// => 이 필터는 Command 구현체를 실행하는 필터이다.
//
public class CommandFilter implements Filter{

  private Command command;

  public CommandFilter(Command command) {
    this.command = command;
  }

  @Override
  public void doFilter(CommandRequest request, CommandResponse response, FilterChain nextChain) throws Exception {


    command.service(request, response);
  }

}
