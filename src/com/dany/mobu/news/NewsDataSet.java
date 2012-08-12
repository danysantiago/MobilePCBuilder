/**
MoBu - Mobile PC Builder, and android application about computer hardware.
    Copyright (C) 2011  Daniel Santiago

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/



package com.dany.mobu.news;


//This class will become the objects to contain the items parsed from the RSS XML
public class NewsDataSet {

	private String title = null;
	private String link = null;
	private String pubDate = null;
	private String imgUrl = null;

	@Override
	public String toString() {
		return "NewsDataSet [title=" + title + ", link=" + link + ", pubDate="
				+ pubDate + ", imgUrl=" + imgUrl + "]";
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPubDate() {
		// When returning the date get only a portion of the whole string but only if available
		if (pubDate.length() >= 16)
			return pubDate.substring(0, 16);
		else
			return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}
