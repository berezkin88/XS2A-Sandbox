import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { GrantConsentComponent } from './grant-consent.component';
import {AccountDetailsComponent} from "../account-details/account-details.component";
import {ReactiveFormsModule} from "@angular/forms";
import {RouterTestingModule} from "@angular/router/testing";

describe('GrantConsentComponent', () => {
  let component: GrantConsentComponent;
  let fixture: ComponentFixture<GrantConsentComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        ReactiveFormsModule
      ],
      declarations: [ GrantConsentComponent, AccountDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GrantConsentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
