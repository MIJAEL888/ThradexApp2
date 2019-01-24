package org.thradex.rrhh.test;

//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = {MvcConfiguration.class, RootConfig.class, MvcWebInitializer.class})
public class ScheduleScaleServiceTest {
	
//	@Autowired
//	ScheduleScaleService scheduleScaleService;
//	
//	@Autowired
//	ShiftDetailService2 shiftDetailService2;
//	
//	@Autowired
//	ShiftDetailService shiftDetailService;
//	
//	@Autowired
//	MailService mailService;
//	
//	@Autowired
//	VelocityEngine velocityEngine;
//	
//	@Autowired
//	MessagesRh messagesRh;
//	
//	@Test
//	public void testJustyAlert() throws Exception{
//		RhShift rhShift = scheduleScaleService.get(3753);
////		RhShift rhShift2 = scheduleScaleService.get(3511);
////		assertEquals(2947, shiftDetailService2.process(rhShift, false));
////		shiftDetailService.process(rhShift, false);
////		shiftDetailService2.process(rhShift, false);
////		shiftDetailService2.process(rhShift2, false);
////		assertEquals("/opt/ThradexApp/shift/", PropertiesSis.PATH_SHIFT_FILES);
//		
//		Set<String> listTO = new HashSet<String>();
//		listTO.add("mijael.palomino@thradex.com");
//		
////		StringWriter mergedContent = new StringWriter();
//		 Map<String, Object> model = new HashMap<>();
//		 model.put("nameTo", rhShift.getRhPersonMng().getFullname());
//		 model.put("message", messagesRh.getMsgMailRequest(rhShift.getRhType().getName(), rhShift.getRhPerson().getFullname()));
//		 model.put("rhShift", rhShift);
//		 model.put("date", new DateTool());
//		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "rrhh/emailTemplate3.vm", "UTF-8", model);
////		
//		mailService.sendMail(listTO, new HashSet<String>(), new HashSet<String>(), "Mail Appmanager 2", text.toString());
//		
//		assertEquals("detail of the mail is here", messagesRh.getMsgMailRequest(rhShift.getRhType().getName(), rhShift.getRhPerson().getFullname()));
//	}
}
