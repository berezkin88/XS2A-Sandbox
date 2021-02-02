import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import {OnlineBankingService} from "../../common/services/online-banking.service";
import {InfoService} from "../../common/info/info.service";
import { PeriodicPaymentsComponent } from './periodic-payments.component';
import {ClipboardModule} from "ngx-clipboard";
import {ReactiveFormsModule} from "@angular/forms";
import {RouterTestingModule} from "@angular/router/testing";
import {InfoModule} from "../../common/info/info.module";
import {PaymentTO} from "../../api/models/payment-to";
import { of } from 'rxjs';

describe('PeriodicPaymentsComponent', () => {
    let component: PeriodicPaymentsComponent;
    let fixture: ComponentFixture<PeriodicPaymentsComponent>;
    let inforService: InfoService;
    let onlineBankingService: OnlineBankingService;
    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            imports: [ReactiveFormsModule, RouterTestingModule, InfoModule, ClipboardModule],
            declarations: [ PeriodicPaymentsComponent ],
            providers: [InfoService, OnlineBankingService]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(PeriodicPaymentsComponent);
        component = fixture.componentInstance;
        inforService = TestBed.get(InfoService);
        onlineBankingService = TestBed.get(OnlineBankingService);
        fixture.detectChanges();
    });

   /* it('should create', () => {
        expect(component).toBeTruthy(); //TODO Fix me!
    });*/

   /* it('should get the Periodic Payments', () => {
        let payments: PaymentTO = {
        }
        let periodicSpy = spyOn(onlineBankingService, 'getPeriodicPaymentsPaged').and.returnValue(of({payments: payments}));
        component.getPeriodicPaymentsPaged(0,1);
        expect(periodicSpy).toHaveBeenCalled();
    });*/
});
