Pod::Spec.new do |s|
  s.name         = 'loggerKit'
  s.version      = '1.1.0-alpha03'
  s.summary      = 'Kotlin Multiplatform logging library for iOS'
  s.homepage     = 'https://github.com/Scarlet-Pan/logger'
  s.license      = { :type => 'MIT', :file => 'LICENSE' }
  s.authors      = { 'Scarlet Pan' => 'scarletpan@qq.com' }
  s.source       = { :git => 'https://github.com/Scarlet-Pan/logger.git', :tag => '1.1.0-alpha03' }
  s.source_files = 'dummy.swift'
  s.vendored_frameworks = 'loggerKit.xcframework'
  s.ios.deployment_target = '12.0'
  s.swift_version = '5.0'
end