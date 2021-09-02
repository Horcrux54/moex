package moex.app.controllers;

import moex.app.inquiries.MyInquiries;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AppController {
    private final MyInquiries myInquiries;

    public AppController(MyInquiries myInquiries) {
        this.myInquiries = myInquiries;
    }

    @GetMapping("shares/{ticker}")
    public String showShares(@PathVariable String ticker,
                             @RequestParam(name = "from", required = false, defaultValue = "0") String from,
                             @RequestParam(name = "till", required = false, defaultValue = "0") String till) throws IOException {
        if(!from.equals("0") && !till.equals("0")){

            return myInquiries.history("shares", ticker, from, till);
        }

        return myInquiries.lastPrice("shares", ticker, 12);
    }

    @GetMapping("bonds/{ticker}")
    public String showBonds(@PathVariable String ticker,
                            @RequestParam(name = "from", required = false, defaultValue = "0") String from,
                            @RequestParam(name = "till", required = false, defaultValue = "0") String till) throws IOException{
        if(!from.equals("0") && !till.equals("0")){

            return myInquiries.history("bonds", ticker, from, till);
        }
        return myInquiries.lastPrice("bonds", ticker, 11);
    }
}
