# SelectorWidget Sample

```kotlin
view.background = SelectorWidget().apply {
      addState(
          RoundedShape(
            state = arrayOf(AndroidState(android.R.attr.state_enabled, false)),
            shape = radius,
            color = getColor(R.color.lm_green_lighter_state_disable)
      ))
      addState(
          RoundedShape(
              shape = ShapeType.singleRounded(2, 8),
              color = getColor(R.color.lm_green),
              rippleColor = getColor(R.color.lm_grey_darker_state_pressed)
          )
      )
 }
```
# CurlInterceptor (for retrofit)
- Sample output
```
2019-04-05 16:50:19.775 19999-23379/{app.name} I/CURL: URL: https://{api_url}/products/123456
    ────────────────────────────────
    Request ID: CurlLogInterceptor@3f552a6
    URL cible: http://{base_api_url}/{api_name}/123456

    cURL -X GET -H "Accept: application/json"
    -H "Accept: application/json"
    -H "Application-Version: 4.14.4-version-SNAPSHOT" 
    -H "Cookie: ...." 
    -H "X-DeviceModel: ONEPLUS A8010" 
    "https://{base_api_url}/{api_name}/123456" -L 
    ────────────────────────────────

```
