package com.haloomin.daylog.member.service;

import com.haloomin.daylog.member.service.dto.command.ReIssueCommand;
import com.haloomin.daylog.member.service.dto.command.SignInCommand;
import com.haloomin.daylog.member.service.dto.command.SignOutCommand;
import com.haloomin.daylog.member.service.dto.command.SignupCommand;
import com.haloomin.daylog.member.service.dto.result.ReIssueResult;
import com.haloomin.daylog.member.service.dto.result.SignInResult;

public interface MemberService {

    void signUp(SignupCommand command);

    SignInResult signIn(SignInCommand command);

    void signOut(SignOutCommand command);

    ReIssueResult reIssue(ReIssueCommand command);

}
