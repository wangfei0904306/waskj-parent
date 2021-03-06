/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.common.captcha.text.renderer;

import com.baomidou.kisso.common.captcha.color.ColorFactory;
import com.baomidou.kisso.common.captcha.font.FontFactory;

import java.awt.image.BufferedImage;

/**
 * 文本渲染接口
 */
public interface TextRenderer {

	void setLeftMargin(int leftMargin);

	void setRightMargin(int rightMargin);

	void setTopMargin(int topMargin);

	void setBottomMargin(int bottomMargin);

	void draw(String text, BufferedImage canvas, FontFactory fontFactory, ColorFactory colorFactory);

}
