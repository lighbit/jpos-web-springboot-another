package com.iso8583.web.webspringjposiso8583.controller;

import com.iso8583.web.webspringjposiso8583.dto.PaymentIsoModel;
import iso8583.helper.SimpleDateFormats;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class PaymentController {
    @Autowired private QMUX qmux;
    SimpleDateFormats set = new SimpleDateFormats();

    @PostMapping("/payment")
    public Map<String, String> topup(@RequestBody @Valid PaymentIsoModel request){
        Map<String, String> hasil = new LinkedHashMap<>();

        try {
            ISOMsg msgRequest = new ISOMsg("0200");
            msgRequest.set(4, request.getAmount().setScale(0).toString());
            msgRequest.set(7, set.formatterBit7.format(new Date()));
            msgRequest.set(11, "000001");
            msgRequest.set(15, set.formatterbit15.format(new Date()));
            msgRequest.set(41,"ATM001KADUGADUNG");

            String bit48 = request.getUniquenumber().substring(0,4);
            bit48 += String.format("%1$" + 13 + "s", request.getUniquenumber().substring(4));
            System.out.println("Bit 48 : ["+bit48+ request.getName() + request.getAccountto()+"]");
            msgRequest.set(48, bit48 + request.getName() + request.getAccountto());

            msgRequest.set(63, "131001");

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

            hasil.put("success", "true");
            hasil.put("response_code", isoResponse.getString(39));
            hasil.put("raw_message", response);
            hasil.put("message: ", "Transaksi a/n " + request.getName() + " dengan accountnumber: "
                    + request.getUniquenumber() + " dengan tujuan akun: " + request.getAccountto()
                    + " Sebesar Rp." + request.getAmount() + " Pada tanggal "
                    + set.formattanggal.format(new Date()) + " pada jam " + set.formatjam.format(new Date()) + " berhasil!");

            System.out.println("Transaksi a/n " + request.getName() + " dgn accountnumber: "
                    + request.getUniquenumber() + " dgn tujuan: " + request.getAccountto()
                    + " Sebesar Rp." +request.getAmount() + " Pada tanggal "  + set.formattanggal.format(new Date())
                    + " jam " + set.formatjam.format(new Date()) + " berhasil!");
        } catch (ISOException e) {
            e.printStackTrace();
        }

        return hasil;
    }
}

