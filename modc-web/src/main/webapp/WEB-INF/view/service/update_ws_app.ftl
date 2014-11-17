<!DOCTYPE html>
<html>
<head>
<#include "../template/header.ftl"/>
    <title><@s.message "webservice.update.ws.app.title" /></title>
</head>
<body>
<!-- Navigation Section -->
<#include "../template/nav.ftl" />
<!-- Navigation Title -->
<div class="page_title">
    <div class="page_title_inline">&nbsp;</div>
    <div class="page_title_inline"><img src="${base}/images/link_arrow.png" border="0"/></div>
    <div class="page_title_inline"><a href="${base}/service/ws_app_list.htm"><@s.message "webservice.list.apps.title" /></a></div>
    <div class="page_title_inline"><img src="${base}/images/link_arrow.png" border="0"/></div>
    <div class="page_title_inline"><@s.message "webservice.update.ws.app.title" /></div>
</div>
<div style="clear:both"></div>
<!-- End of Navigation Title -->

<div class="main_body_div">
    <div class="main_middle_div">

        <!-- left panel -->
        <div class="left_display_div">
            <div style="clear:both"></div>
            <div class="left_display_inner">
            <#include "../template/action_message.ftl" />
            <@sf.form action="update_ws_app.htm" commandName="serviceAppBean" method="post">
                <@sf.hidden path="serviceApp.id" />
                <@sf.hidden path="serviceApp.uniqueId" />
                <div class="input_row_section">
                    <div class="input_row_left_part">App Name:</div>
                    <div class="input_row_right_part">
                        <@sf.input path="serviceApp.name" /> <span class="red_span"> *</span>
                    </div>
                    <div style="clear:both"></div>
                    <div class="input_row_comments">
                        <@s.message "webservice.app.name.comment" />
                    </div>
                </div>
                <div style="clear:both"></div>

                <!-- creators -->
                <div class="input_row_section">
                    <div class="input_row_left_part">App Id:</div>
                    <div class="input_row_right_part">
                    ${serviceAppBean.serviceApp.uniqueId}
                    </div>
                </div>

                <!-- creators -->
                <div class="input_row_section">
                    <div class="input_row_left_part">Service Type:</div>
                    <div class="input_row_right_part">
                        <@sf.select path="serviceApp.serviceType" class="input_select_small">
                            <@sf.option value="none"> --- Select --- </@sf.option>
                            <@sf.option value="doi">doi</@sf.option>
                            <@sf.option value="md">metadata</@sf.option>
                        </@sf.select> <span class="red_span"> *</span>
                    </div>
                    <div style="clear:both"></div>
                    <div class="input_row_comments">
                        <@s.message "webservice.app.service.type.comment" />
                    </div>
                </div>

                <div class="input_row_section">
                    <div class="input_row_left_part">App Description:</div>
                    <div class="input_row_right_part">
                        <@sf.textarea path="serviceApp.description" cssStyle="width: 400px; height: 80px;" cssClass="input_textarea" /> <span class="red_span"> *</span>
                    </div>
                    <div style="clear:both"></div>
                    <div class="input_row_comments">
                        <@s.message "webservice.app.desc.comment" />
                    </div>
                </div>

                <div class="dinput_row_section">
                    <div class="dfield_left_part">Authorized IPs:</div>
                    <div class="dfield_right_part">
                        Add IP <img class="dfield_img" src="${base}/images/add.png" border="0" id="add_ip"/>
                    </div>
                    <div style="clear:both"></div>
                    <div class="dfield_comments">
                        <@s.message "webservice.app.add.ip.comment" />
                    </div>
                </div>
                <div class="input_row_section">
                    <div class="input_row_left_part">&nbsp;</div>
                    <div class="input_row_right_part">
                        <table class="add_ip_tab" id="add_ip_tab" border="0">
                            <thead>
                            <tr>
                                <th width="50%">IP Address</th>
                                <th width="50%">&nbsp;</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#if (serviceAppBean.serviceAuthIPs?? && serviceAppBean.serviceAuthIPs?size > 0)>
                                    <#list serviceAppBean.serviceAuthIPs as sai>
                                    <tr>
                                        <td>
                                            <div class="input_row_value_div">
                                                <@sf.hidden path="serviceAuthIPs[${sai_index}].id" id="authIpId" />
                                                <@sf.input path="serviceAuthIPs[${sai_index}].ipAddress" id="ipAddress" />
                                            </div>
                                        </td>
                                        <td width="50"><img class="input_row_value_img" src="${base}/images/delete.png" title='remove' id="remove_ip"/></td>
                                    </tr>
                                    </#list>
                                <#else>
                                <tr>
                                    <td>
                                        <div class="input_row_value_div">
                                            <@sf.hidden path="serviceAuthIPs[0].id" id="authIpId" />
                                            <@sf.input path="serviceAuthIPs[0].ipAddress" id="ipAddress" />
                                        </div>
                                    </td>
                                    <td><img class="input_row_value_img" src="${base}/images/delete.png" title='remove' id="remove_ip"/></td>
                                </tr>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div style="clear:both"></div>

                <div style="clear:both"></div>
                <div class="input_row_section">
                    <div class="input_row_left_part">&nbsp;</div>
                    <div class="input_row_right_part">
                        <input type="submit" value="Update" class="input_button_style"/>
                    </div>
                </div>
                <div style="clear:both"></div>
            </@sf.form>
            </div>
        </div>
        <!-- right panel -->
        <div class="right_display_div">
        <#if authen_flag?? && authen_flag == 'authenticated'>
    <#include "../template/sub_nav.ftl" />
</#if>
        </div>
    </div>
</div>
</div>
<div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>