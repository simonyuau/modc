<!DOCTYPE html>
<html>
<head>
<#include "../template/header.ftl"/>
    <title><@s.message "webservice.ws.app.title" /></title>
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
    <div class="page_title_inline"><@s.message "webservice.ws.app.title" /></div>
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
                <div class="input_row_section">
                    <div class="input_row_left_part">App Name:</div>
                    <div class="input_row_right_part">
                    ${serviceAppBean.serviceApp.name}
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
                    ${serviceAppBean.serviceApp.serviceType}
                    </div>
                </div>

                <div class="input_row_section">
                    <div class="input_row_left_part">App Description:</div>
                    <div class="input_row_right_part">
                    ${serviceAppBean.serviceApp.description}
                    </div>
                </div>

                <div class="input_row_section">
                    <div class="input_row_left_part">Authorized IPs:</div>
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
                                                ${sai.ipAddress}
                                            </div>
                                        </td>
                                        <td></td>
                                    </tr>
                                    </#list>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div style="clear:both"></div>
                <div class="input_row_section">
                    <div class="input_row_left_part">&nbsp;</div>
                    <div class="input_row_right_part">
                        <div class="data_action_link">
                            <a href="${base}/service/update_ws_app.htm?id=${serviceAppBean.serviceApp.id?c}"> Update </a>
                        </div>
                        <div class="data_action_link">
                            <a href="${base}/service/delete_ws_app.htm?id=${serviceAppBean.serviceApp.id?c}"> Delete </a>
                        </div>
                    </div>
                </div>
                <div style="clear:both"></div>
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