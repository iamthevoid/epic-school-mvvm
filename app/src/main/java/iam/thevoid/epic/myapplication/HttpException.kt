package iam.thevoid.epic.myapplication

import java.lang.Exception

class HttpException(code: Int) : Exception("Received illegal code $code")