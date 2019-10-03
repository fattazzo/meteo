/*
 * Project: meteo
 * File: IssueReporter.kt
 *
 * Created by fattazzo
 * Copyright © 2019 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gmail.fattazzo.meteo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

import com.gmail.fattazzo.meteo.R
import java.io.Serializable

import java.net.URLEncoder

/**
 * @author fattazzo
 *
 *
 * date: 01/08/17
 */
object IssueReporter : Serializable {

    private const val REPORT_LINK = "https://gitreports.com/issue/fattazzo/meteo"

    private const val QUERY_ISSUE_TITLE = "issue_title"
    private const val QUERY_ISSUE_DETAILS = "details"

    /**
     * Open report issue page in browser.
     *
     * @param context context
     */
    fun openReportIssue(context: Context, issueTitle: String?, issueDetails: String?, addInfo: Boolean) {
        var issueDetails = issueDetails

        if (addInfo) {
            issueDetails = issueDetails.orEmpty() + getInfo(context)
        }

        val i = Intent(Intent.ACTION_VIEW)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.data = Uri.parse(buildUrl(issueTitle, issueDetails))
        context.startActivity(i)
    }

    private fun getInfo(context: Context): String {

        val info = DeviceInfo(context)
        val bugReportUserRequest = context.resources.getString(R.string.bug_report_user_request)

        return "\n" +
                "---\n" +
                bugReportUserRequest +
                "\n" +
                info.toMarkdown()
    }

    private fun buildUrl(issueTitle: String?, issueDetails: String?): String {
        return try {
            REPORT_LINK +
                    "?" +
                    QUERY_ISSUE_TITLE +
                    "=" +
                    URLEncoder.encode(issueTitle.orEmpty(), "UTF-8") +
                    "&" +
                    QUERY_ISSUE_DETAILS +
                    "=" +
                    URLEncoder.encode(issueDetails.orEmpty(), "UTF-8")
        } catch (e: Exception) {
            REPORT_LINK
        }

    }


}
