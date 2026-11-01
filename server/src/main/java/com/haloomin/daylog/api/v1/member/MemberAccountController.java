package com.haloomin.daylog.api.v1.member;

import com.haloomin.daylog.api.v1.common.dto.ResponseV1;
import com.haloomin.daylog.api.v1.member.dto.request.AccountReissueRequestV1;
import com.haloomin.daylog.api.v1.member.dto.request.AccountSignInRequestV1;
import com.haloomin.daylog.api.v1.member.dto.request.AccountSignOutRequestV1;
import com.haloomin.daylog.api.v1.member.dto.request.AccountSignupRequestV1;
import com.haloomin.daylog.api.v1.member.dto.response.AccountReissueResponseV1;
import com.haloomin.daylog.api.v1.member.dto.response.AccountSignInResponseV1;
import com.haloomin.daylog.member.service.MemberService;
import com.haloomin.daylog.member.service.dto.result.ReIssueResult;
import com.haloomin.daylog.member.service.dto.result.SignInResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@RestController
public class MemberAccountController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseV1<Void>> signUp(@Valid @RequestBody AccountSignupRequestV1 requestDto) {
        memberService.signUp(requestDto.toCommand());
        return ResponseEntity.ok(ResponseV1.success());
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseV1<AccountSignInResponseV1>> signIn(@Valid @RequestBody AccountSignInRequestV1 requestDto) {
        SignInResult signInResult = memberService.signIn(requestDto.toCommand());
        AccountSignInResponseV1 response = AccountSignInResponseV1.from(signInResult);
        return ResponseEntity.ok(ResponseV1.success(response));
    }

    @PostMapping("/signout")
    public ResponseEntity<ResponseV1<Void>> signOut(@Valid @RequestBody AccountSignOutRequestV1 request) {
        memberService.signOut(request.toCommand());
        return ResponseEntity.ok(ResponseV1.success());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseV1<AccountReissueResponseV1>> reIssue(
            @Valid @RequestBody AccountReissueRequestV1 requestDto
    ) {
        ReIssueResult reIssueResult = memberService.reIssue(requestDto.toCommand());
        AccountReissueResponseV1 response = AccountReissueResponseV1.from(reIssueResult);
        return ResponseEntity.ok(ResponseV1.success(response));
    }
}
