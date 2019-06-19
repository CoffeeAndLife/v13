<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello,${username}
    <hr/>
    id:${student.id}
    name:${student.name}
    entryDate:${student.entryDate?date}
    entryDate:${student.entryDate?time}
    entryDate:${student.entryDate?datetime}

    <hr/>
    <table>
        <tr>
            <td>ID</td>
            <td>name</td>
            <td>entryDate</td>
        </tr>
        <#list list as stu>
            <tr>
                <td>${stu.id}</td>
                <td>${stu.name}</td>
                <td>${stu.entryDate?date}</td>
            </tr>
        </#list>
    </table>

    <hr/>
    <#if (money>=1000000)>
        小康状态
        <#elseif (money>=100000)>
        脱贫状态
        <#else >
        你该想想为什么跟别人差距这么大
    </#if>

    <hr/>
    ${msg!}
    ${msg!'没有任何消息'}

</body>
</html>