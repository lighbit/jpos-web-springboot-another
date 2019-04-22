package com.iso8583.web.webspringjposiso8583.controller;

import com.iso8583.web.webspringjposiso8583.dto.BaseIsoModel;
import com.iso8583.web.webspringjposiso8583.dto.PaymentIsoModel;
import iso8583.helper.SimpleDateFormats;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class PaymentController {

    @Autowired private QMUX qmux;

//    @Value("#{iso8583['iso.channel.id']}")
//    private String channelId;
//
//    @Value("#{iso8583['iso.terminal.id']}")
//    public String termIdTransfer;
//
//    @Value("#{iso8583['iso.terminal.id.other']}")
//    public String termIdOther;
//
//    @Value("#{iso8583['iso.bit32']}")
//    public String bit32;
//
//    @Value("#{iso8583['iso.teller.id']}")
//    private String tellerId;

    @Value("#{data.payment.message.text.iso8583['iso.channel.id']}")
    public String channelId;

    SimpleDateFormats set = new SimpleDateFormats();
    public int stan= 0;

    @PostMapping("/sue")
    public Map<String, String> topup(@RequestBody @Valid BaseIsoModel request){

        PaymentIsoModel isoModel = new PaymentIsoModel(request.getAtmcardnumber(), request.getAccountnumber(), request.getAmount(), request.getName());

        Map<String, String> hasil = new LinkedHashMap<>();

        Date date = new Date();
        String referenceNumber = stan + "" + ISODate.getTime(date);

        try {
            ISOMsg msgRequest = new ISOMsg("0200");
            msgRequest.set(2, request.getAtmcardnumber());
            msgRequest.set(4, request.getAmount());
            msgRequest.set(7, set.formatterBit7.format(new Date()));
            msgRequest.set(11, String.format("%06d", stan++));
            msgRequest.set(12, ISODate.getTime(date));
            msgRequest.set(15, set.formatterbit15.format(new Date()));
            msgRequest.set(18, channelId);
            msgRequest.set(37, referenceNumber);
            msgRequest.set(41, "ATM001KADUGADUNG");
//            msgRequest.set(43, tellerId);
            msgRequest.set(49, "360");
            msgRequest.set(102, request.getAccountnumber());

            // TODO: Get bit 48
            String bit48 = isoModel.getUniquenumber().substring(0,4);
            bit48 += String.format("%1$" + 13 + "s", isoModel.getUniquenumber().substring(4).trim()
                    + isoModel.getAccountto().trim() + request.getAmount().trim() + request.getName().trim());
            System.out.println("Bit 48 : ["+bit48+"]");
            msgRequest.set(48, bit48);

            LinkedHashMap<String, Object> detailPayment = new LinkedHashMap<String, Object>();

            detailPayment.put("NO_HP", isoModel.getUniquenumber());
            detailPayment.put("AKUN_KE", isoModel.getAccountto());
            detailPayment.put("SEBESAR", isoModel.getAmount());

            isoModel.setDetailPayment(detailPayment);

            ISOMsg isoResponse = qmux.request(msgRequest, 20 * 1000);

            if(isoResponse == null){
                // buat message reversal
                // kirim reversal
                // kalau masih timeout, ulangi 2x lagi reversal

                hasil.put("success", "false");
                hasil.put("error", "timeout");
                return hasil;
            }

            String response = new String(isoResponse.pack());

            hasil.put("detailpayment", isoModel.getDetailPayment().toString());
            hasil.put("message", "Pengiriman pulsa dengan nomor " + isoModel.getUniquenumber() + " sebesar Rp."
                    + request.getAmount() + " Pada tanggal " + set.formattanggal.format(new Date())
                    + " jam " + set.formatjam.format(new Date())+ " Berhasil!");

            System.out.println("Pengiriman pulsa dengan nomor " + isoModel.getUniquenumber() + " sebesar Rp."
                    + request.getAmount() + " Pada tanggal " + set.formattanggal.format(new Date())
                    + " jam " + set.formatjam.format(new Date())+ " Berhasil!");
        } catch (ISOException e) {
            e.printStackTrace();
        }

        return hasil;
    }

//    SimpleDateFormats set = new SimpleDateFormats();
//    public int stan= 0;
//
//    @PostMapping("/wew")
//    public Map<String, Object> topup(@RequestBody @Valid BaseIsoModel request){
//        PaymentIsoModel isoModel = new PaymentIsoModel(request.getAtmcardnumber(), request.getAccountnumber(), request.getAmount(), request.getName());
//
//        Container container = new Container();
//        container.execute(request);
//        // TODO: Get bit 48
//        String bit48 = request.getMsisdn().substring(0,4);
//        bit48 += String.format("%1$" + 13 + "s", request.getMsisdn().substring(4));
//        System.out.println("Bit 48 : ["+bit48+"]");
//        msgRequest.set(48, bit48);
//
//        Map<String, Object> detailpayment = new LinkedHashMap<>();
//
//        detailpayment.put("NO_HP", isoModel.getUniquenumber());
//        detailpayment.put("AKUN_KE", isoModel.getAccountto());
//        detailpayment.put("SEBESAR", isoModel.getAmount());
//
//        request.setDetailPayment(detailpayment);
//
//        System.out.println("Pengiriman pulsa dengan nomor " + isoModel.getUniquenumber() + " sebesar Rp."
//                + request.getAmount() + " Atas Nama " + isoModel.getName() + " Pada tanggal " + set.formattanggal.format(new Date())
//                + " jam " + set.formatjam.format(new Date())+ " Berhasil!");
//
//        return request.getDetailPayment();
//    }
}

