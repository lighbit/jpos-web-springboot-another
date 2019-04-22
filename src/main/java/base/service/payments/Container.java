package base.service.payments;

import com.iso8583.web.webspringjposiso8583.dto.BaseIsoModel;
import com.iso8583.web.webspringjposiso8583.dto.PaymentIsoModel;
import iso8583.helper.SimpleDateFormats;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
//@RequestMapping("/api/transaction/payment")
public class Container {
    @Autowired
    private QMUX qmux;

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

    SimpleDateFormats set = new SimpleDateFormats();
    private int stan = 0;

    @PostMapping("/wew")
//    @ResponseBody
    public Map<String, String> execute(@RequestBody @Valid BaseIsoModel request) {
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
//            msgRequest.set(18, channelId);
            msgRequest.set(37, referenceNumber);
            msgRequest.set(41, "ATM001KADUGADUNG");
//            msgRequest.set(43, tellerId);
            msgRequest.set(49, "360");
            msgRequest.set(102, request.getAccountnumber());

            //TODO: FOT BIT 48
//            msgRequest.set(48, response);

//            /**
//             * TODO: MAKE A SAMPLE
//             */
//            PaymentIsoModel isoModel = new PaymentIsoModel(request.getAtmcardnumber(), request.getAccountnumber(), request.getAmount(), request.getName());
//
//
//            Map<String, Object> detailPayment = new LinkedHashMap<String, Object>();
//
//            detailPayment.put("NO_HP", isoModel.getUniquenumber());
//            detailPayment.put("AKUN_KE", isoModel.getAccountto());
//            detailPayment.put("SEBESAR", isoModel.getAmount());
//
//            request.setDetailPayment(detailPayment);
//
//            System.out.println("Pengiriman pulsa dengan nomor " + isoModel.getUniquenumber() + " sebesar Rp."
//                    + request.getAmount() + " Atas Nama " + isoModel.getName() + " Pada tanggal " + set.formattanggal.format(new Date())
//                    + " jam " + set.formatjam.format(new Date())+ " Berhasil!");
//            /**
//             * TODO: UNTIL THIS LINE
//             */


            ISOMsg isoResponse = qmux.request(msgRequest, 20 * 1000);

            if (isoResponse == null) {
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
//            hasil.put("detailpayment", request.getDetailPayment().toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasil;
    }
}
