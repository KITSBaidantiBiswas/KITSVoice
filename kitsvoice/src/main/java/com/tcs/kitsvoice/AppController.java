package com.tcs.kitsvoice;

import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.twilio.http.HttpMethod;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.messaging.Redirect;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Pause;
import com.twilio.twiml.voice.Say;

@Controller
public class AppController {

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String greeting() {
		return "hello world";
	}

	@RequestMapping(value = "/attendCall", method = RequestMethod.POST, produces = { "application/xml" })
	@ResponseBody
	public String attendCall() {

		Say say = new Say.Builder("Hello, How can I help you today?").build();
		Gather gather = new Gather.Builder().inputs(Arrays.asList(Gather.Input.SPEECH)).action("/completed").partialResultCallback("/partial").method(HttpMethod.POST).speechTimeout("auto").numDigits(1).say(say).build();
		Say say2 = new Say.Builder("We didn't receive any input. Goodbye!").build();

		VoiceResponse response = new VoiceResponse.Builder().gather(gather).say(say2).build();

		try {
			System.out.println(response.toXml());
		} catch (TwiMLException e) {
			e.printStackTrace();
		}
		return response.toXml();

		// Contact number: 448081689319
	}

	@RequestMapping(value = "/completed", method = RequestMethod.POST, produces = { "application/xml" })
	@ResponseBody
	public String finalresult(@RequestBody String r, @RequestParam("SpeechResult") String speechResult, @RequestParam("Confidence") double confidence) {
		System.out.println(speechResult);
		System.out.println(confidence);
		return "<Response><Say>You have said " + speechResult + ". Thank you</Say></Response>";
	}
	

	@RequestMapping(value = "/partial", method = RequestMethod.POST, produces = { "application/xml" })
	@ResponseBody
	public void partialresult(@RequestBody String r, @RequestParam("UnstableSpeechResult") String speechResult, @RequestParam("Stability") double stability) {
		System.out.println(speechResult);
		System.out.println(stability);
	}
}
