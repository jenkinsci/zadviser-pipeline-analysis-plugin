
# zAdviser Pipeline Analysis Plugin

## Introduction
**BMC AMI zAdviser** is a mainframe analytics platform that collects data from 
development tools, pipelines, and production systems, then applies analytics and machine learning 
to measure software-delivery performance. zAdviser captures a broad range of metrics to help teams 
improve quality, velocity, and efficiency — including usage of specific BMC tools and tool functions,
source code management (SCM) KPIs, and diverse DevOps performance parameters across teams, individual
users, and applications.

**zAdviser Pipeline Analysis Plugin** captures key CI/CD pipeline data and sends it to zAdviser, enabling clear 
visualization of delivery performance and testing, helping teams spot trends, bottlenecks, and improvement 
opportunities.

## Prerequisites
The following are required to use this plugin:
- Jenkins (v2.479.3 or higher)
- Jenkins Pipeline: REST API plugin v2.38 or higher. 
 ```
Note: This plugin will be installed automatically when you try to install the analysis plugin. However, since there might be some versioning mismatch causing Jenkins to not load properly after a restart, we recommend manually updating the REST API plugin first.
```
- BMC AMI zAdviser Enterprise On-Prem (v26.04.00 or higher)
- Optionally for test related data, we require the publishing of test results via the JUnitg publisher ([JUnit Plugin](https://plugins.jenkins.io/junit/))

## Configuration

Under **Manage Jenkins > System > zAdviser Pipeline Analysis Plugin Configuration**
Please check the zAdviser documentation for further details

```text
We recommend enabling HTTPS for both zAdviser and your Jenkins instance.
Steps for zAdviser are mentioned in the product documentation
```

## Product Assistance

BMC provides assistance to customers with its documentation, the BMC Support website, and via telephone calls with the Customer Support team.

### BMC Support Central

You can access information about BMC products via our Support site, [https://support.bmc.com](https://support.bmc.com/). Support Central provides access to critical information about your BMC products. You can review frequently asked questions, read or download documentation, access product fixes, or e-mail your questions or comments. The first time you access Support Central, you must register and obtain a password. The registration is free.

### Contacting Customer Support

At BMC, we strive to make our products and documentation the best in the industry. Feedback from our customers helps us maintain our quality standards. If you need support services, please obtain the following information before calling BMC/'s 24-hour telephone support:

- The Jenkins pipeline job output that contains any error messages or pertinent information. Relevant errors will be prefixed with `[zAdviser Pipeline Analysis Plugin]`
- The zAdviser logs, found in `your-instllation-directory/logs/`
- The name, release number, and build number of your product. This information is displayed in the installed extensions page. Apply the filter, BMC, to display all the installed BMC extensions.

### Web

You can report issues via the BMC Support website: [https://support.bmc.com](https://support.bmc.com/).

Note: Please report all high-priority issues by phone.

### Corporate Website

To access the BMC website, go to [https://www.bmc.com/](https://www.bmc.com/). The BMC site provides a variety of product and support information.