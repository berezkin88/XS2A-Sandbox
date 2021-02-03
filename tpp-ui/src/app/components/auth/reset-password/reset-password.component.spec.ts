import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ResetPasswordComponent } from './reset-password.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {By} from "@angular/platform-browser";
import {AuthService} from "../../../services/auth.service";
import {DebugElement} from "@angular/core";

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;
  let authService: AuthService;
  let authServiceSpy;
  let de: DebugElement;
  let el: HTMLElement;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        HttpClientTestingModule,
        RouterTestingModule,
        BrowserAnimationsModule,
      ],
      declarations: [ ResetPasswordComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    authService = fixture.debugElement.injector.get(AuthService);

    de = fixture.debugElement.query(By.css('form'));
    el = de.nativeElement;

    fixture.detectChanges();
    component.ngOnInit();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('loginForm should be invalid when at least one field is empty', () => {
    expect(component.resetPasswordForm.valid).toBeFalsy();
  });

  it('email field validity', () => {
    let errors = {};
    const email = component.resetPasswordForm.controls['email'];
    expect(email.valid).toBeFalsy();

    // email field is required
    errors = email.errors || {};
    expect(errors['required']).toBeTruthy();

    // set email to something correct
    email.setValue('test@test.de');
    errors = email.errors || {};
    expect(errors['required']).toBeFalsy();
  });

  it('login field validity', () => {
    let errors = {};
    const login = component.resetPasswordForm.controls['login'];
    expect(login.valid).toBeFalsy();

    // login field is required
    errors = login.errors || {};
    expect(errors['required']).toBeTruthy();

    // set login to something correct
    login.setValue('foo');
    errors = login.errors || {};
    expect(errors['required']).toBeFalsy();
  });

  it('Should set error message', () => {
    component.onSubmit();
    expect(component.errorMessage).toEqual('Please enter your credentials');
  });

  // TODO write unite tests https://git.adorsys.de/adorsys/xs2a/psd2-dynamic-sandbox/-/issues/704

});
