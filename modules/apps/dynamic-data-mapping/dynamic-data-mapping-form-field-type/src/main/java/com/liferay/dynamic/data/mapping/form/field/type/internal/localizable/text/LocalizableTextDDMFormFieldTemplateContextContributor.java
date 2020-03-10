/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.mapping.form.field.type.internal.localizable.text;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTemplateContextContributor;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.render.DDMFormFieldRenderingContext;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true, property = "ddm.form.field.type.name=localizable_text",
	service = {
		DDMFormFieldTemplateContextContributor.class,
		LocalizableTextDDMFormFieldTemplateContextContributor.class
	}
)
public class LocalizableTextDDMFormFieldTemplateContextContributor
	implements DDMFormFieldTemplateContextContributor {

	@Override
	public Map<String, Object> getParameters(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		Map<String, Object> parameters = new HashMap<>();

		if (ddmFormFieldRenderingContext.isReturnFullContext()) {
			parameters.put("availableLocales", getAvailableLocalesJSONArray());

			DDMForm ddmForm = ddmFormField.getDDMForm();

			JSONObject localeJSONObject = getLocaleJSONObject(
				ddmForm.getDefaultLocale());

			parameters.put("defaultLocale", localeJSONObject);

			parameters.put("displayStyle", getDisplayStyle(ddmFormField));
			parameters.put("editingLocale", localeJSONObject);
			parameters.put(
				"placeholder",
				getPlaceholder(ddmFormField, ddmFormFieldRenderingContext));
			parameters.put(
				"tooltip",
				getTooltip(ddmFormField, ddmFormFieldRenderingContext));
		}

		String predefinedValue = getPredefinedValue(
			ddmFormField, ddmFormFieldRenderingContext);

		if (predefinedValue != null) {
			parameters.put("predefinedValue", predefinedValue);
		}

		parameters.put("value", getValue(ddmFormFieldRenderingContext));

		return parameters;
	}

	protected JSONArray getAvailableLocalesJSONArray() {
		JSONArray jsonArray = jsonFactory.createJSONArray();

		Set<Locale> locales = language.getAvailableLocales();

		Stream<Locale> stream = locales.stream();

		stream.map(
			this::getLocaleJSONObject
		).forEach(
			jsonArray::put
		);

		return jsonArray;
	}

	protected String getDisplayStyle(DDMFormField ddmFormField) {
		return GetterUtil.getString(
			ddmFormField.getProperty("displayStyle"), "singleline");
	}

	protected JSONObject getLocaleJSONObject(Locale locale) {
		JSONObject jsonObject = jsonFactory.createJSONObject();

		String languageId = LocaleUtil.toLanguageId(locale);

		jsonObject.put(
			"displayName", locale.getDisplayName(locale)
		).put(
			"icon",
			StringUtil.replace(
				languageId, '_', "-"
			).toLowerCase()
		).put(
			"localeId", languageId
		);

		return jsonObject;
	}

	protected String getPlaceholder(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue localizedValue =
			(LocalizedValue)ddmFormField.getProperty("placeholder");

		if (localizedValue == null) {
			return StringPool.BLANK;
		}

		return localizedValue.getString(
			ddmFormFieldRenderingContext.getLocale());
	}

	protected String getPredefinedValue(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue localizedValue = ddmFormField.getPredefinedValue();

		if (localizedValue == null) {
			return null;
		}

		return localizedValue.getString(
			ddmFormFieldRenderingContext.getLocale());
	}

	protected String getTooltip(
		DDMFormField ddmFormField,
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		LocalizedValue localizedValue =
			(LocalizedValue)ddmFormField.getProperty("tooltip");

		if (localizedValue == null) {
			return StringPool.BLANK;
		}

		return localizedValue.getString(
			ddmFormFieldRenderingContext.getLocale());
	}

	protected JSONObject getValue(
		DDMFormFieldRenderingContext ddmFormFieldRenderingContext) {

		try {
			return jsonFactory.createJSONObject(
				ddmFormFieldRenderingContext.getValue());
		}
		catch (JSONException jsonException) {
			if (_log.isDebugEnabled()) {
				_log.debug(jsonException, jsonException);
			}
		}

		return jsonFactory.createJSONObject();
	}

	@Reference
	protected JSONFactory jsonFactory;

	@Reference
	protected Language language;

	private static final Log _log = LogFactoryUtil.getLog(
		LocalizableTextDDMFormFieldTemplateContextContributor.class);

}