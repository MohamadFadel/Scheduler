package com.wavemark.scheduler.cron.dto;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import lombok.RequiredArgsConstructor;
import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FrequencyDescription {

	private final CronExpressionService cronExpressionService;

	public String describe(String cronExpressionStr) {

    CronDescription cronDescription;
    try {
      cronDescription = cronExpressionService.reverseCronExpression(cronExpressionStr, SecurityUtilsV2.getTimezone().getId());
    } catch (CronExpressionException e) {
      return cronExpressionStr;
    }

    return describe(cronDescription);
	}

	public String describe(CronDescription cronDescription) {

		String frequencyDescription;
		try {
			Options options = new Options();
			options.setZeroBasedDayOfWeek(false);
			String cronDescribed = CronExpressionDescriptor.getDescription(cronExpressionService.createCronExpressionStr(cronDescription), options);

			DateTimeFormatter zoneAbbreviationFormatter = DateTimeFormatter.ofPattern("zzz", Locale.ENGLISH);
			String time = ZonedDateTime.now(SecurityUtilsV2.getTimezone()).format(zoneAbbreviationFormatter);

			cronDescribed = cronDescribed.concat(" (" + time + ")");

			frequencyDescription = cronDescription.getFrequency().getCapitalizedCronExpression() + ", " + cronDescribed;
		} catch (ParseException | CronExpressionException e) {
			return cronDescription.getFrequency().getCapitalizedCronExpression();
		}
		return frequencyDescription;
	}
}
