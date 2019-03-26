package com.iso8583.web.webspringjposiso8583.controller;

import com.iso8583.web.webspringjposiso8583.dto.TransferIsoModel;
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
public class TransferController {
    @Autowired private QMUX qmux;
    SimpleDateFormats set = new SimpleDateFormats();
    public int stan= 0;

    @PostMapping("/transfer")
    public Map<String, String> topup(@RequestBody @Valid TransferIsoModel request){
        Map<String, String> hasil = new LinkedHashMap<>();

        try {
            ISOMsg msgRequest = new ISOMsg("0200");
            msgRequest.set(4, request.getAmount().setScale(0).toString());
            msgRequest.set(7, set.formatterBit7.format(new Date()));
            msgRequest.set(11, String.format("%06d", stan++));
            msgRequest.set(15, set.formatterbit15.format(new Date()));

            // TODO: Get bit 48
            String bit48 = request.getAccountnumber().substring(0,4);
            bit48 += String.format("%1$" + 13 + "s", request.getAccountnumber().substring(4));
            System.out.println("Bit 48 : ["+bit48+request.getName()+"]");
            msgRequest.set(48, bit48 + request.getName());

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
            hasil.put("message: ", "Transaksi atas nama " + request.getName() + " dengan accountnumber: "
                    + request.getAccountnumber() +" Sebesar Rp." +request.getAmount() + " Pada tanggal "
                    + set.formattanggal.format(new Date()) + " jam " + set.formatjam.format(new Date()) + " berhasil!");

            System.out.println("Transaksi atas nama " + request.getName() + " dengan accountnumber: "
                    + request.getAccountnumber() +" Sebesar Rp." +request.getAmount() + " Pada tanggal "
                    + set.formattanggal.format(new Date()) + " jam " + set.formatjam.format(new Date()) + " berhasil!");
        } catch (ISOException e) {
            e.printStackTrace();
        }

        return hasil;
    }
}
